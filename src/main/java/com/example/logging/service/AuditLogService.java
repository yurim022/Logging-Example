package com.example.logging.service;

import com.example.logging.Constant;
import com.example.logging.annotation.Audit;
import com.example.logging.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j(topic = "audit")
@Service
public class AuditLogService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Value("${spring.application.name}")
    String applicationName;
    @Value("${yurim.proj.service-code}")
    String serviceCode;

    @Value("${yurim.proj.domain}")
    String serviceDomain;

    String hostName;
    String hostAddress;
    AuditLogHost auditHost;

    @PostConstruct
    public void init() {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            hostAddress = InetAddress.getLocalHost().getHostAddress();
            auditHost = AuditLogHost.builder()
                    .name(hostName)
                    .ip(hostAddress)
                    .build();
        } catch (Exception e) {
            log.warn("Fail to init AuditLogService",e);
        }
    }


    public void logAudit(HandlerMethod handlerMethod, HttpServletRequest request,
                         HttpServletResponse response, Exception ex) throws Exception{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuditLogBase.AuditLogBaseBuilder builder = newAuditLogBase()
                .operation(handlerMethod.getMethod().getName())
                .url(request.getRequestURI().toString())
                .response(buildAuditResponse(request,response,ex))
                .user(AuditLogUser.builder()
                        .id((authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) ? authentication.getName() : getCustomAttribute(request, Audit.AUDIT_LOGIN_USER_ID))
                        .type("user")
                        .ip(request.getRemoteAddr())
                        .agent(request.getHeader(HttpHeaders.USER_AGENT))
                        .build());

        Audit auditInfo = handlerMethod.getMethodAnnotation(Audit.class); //java reflection 으로 method 객체 Audit 가져옴

        if (auditInfo != null) {
            builder.serviceDomain(serviceDomain)
                    .security(AuditLogSecurity.builder()
                            .type(auditInfo.type().name())
                            .event(auditInfo.event().name())
                            .target(getCustomAttribute(request,Audit.AUDIT_REQUEST_TARGET))
                            .detail(getCustomAttribute(request,Audit.AUDIT_REQUEST_DETAIL,auditInfo.detail()))
                            .reason(auditInfo.reason())
                            .personalInfoList(auditInfo.personalInfoList())
                            .build());
        }

        log.info(builder.build().toString());
    }

    private AuditLogResponse buildAuditResponse(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        String type = "SUCCESS";
        if (httpStatus.is4xxClientError()) {
            type = "CLIENT_ERROR";
        } else if (httpStatus.is5xxServerError()) {
            type = "SERVER_ERROR";
        }


        return AuditLogResponse.builder()
                .code(httpStatus.toString())
                .type(type)
                .desc(ex != null ? ex.getMessage() : null)
                .duration(getDuration(request))
                .build();
    }

    private String getDuration(HttpServletRequest request) {
        if (request.getAttribute(Audit.AUDIT_REQUEST_TIME) != null) {
            return (System.currentTimeMillis() - (long) request.getAttribute(Audit.AUDIT_REQUEST_TIME)) + "";
        } else {
            return null;
        }
    }

    private AuditLogBase.AuditLogBaseBuilder newAuditLogBase() {
        return AuditLogBase.builder()
                .timestamp(LocalDateTime.now().format(DATETIME_FORMATTER))
                .service(serviceCode)
                .transactionId(MDC.get(Constant.PROJ_TRANSACTION_ID) != null ? MDC.get(Constant.PROJ_TRANSACTION_ID) : UUID.randomUUID().toString())
                .logType("IN_MSG")
                .host(auditHost);
    }

    private String getCustomAttribute(HttpServletRequest request, String attributeKey) {
        return request.getAttribute(attributeKey) != null ? (String) request.getAttribute(attributeKey) : null;
    }

    private String getCustomAttribute(HttpServletRequest request, String attributeKey, String defaultString) {
        return request.getAttribute(attributeKey) != null ? (String) request.getAttribute(attributeKey) : defaultString;
    }


}
