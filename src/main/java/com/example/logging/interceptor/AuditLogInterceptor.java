package com.example.logging.interceptor;

import com.example.logging.Constant;
import com.example.logging.annotation.Audit;
import com.example.logging.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class AuditLogInterceptor implements HandlerInterceptor {

    @Value("${spring.application.name}")
    String applicationName;

    private final AuditLogService auditLogService;

    //컨트롤러에 진입하기 전에 실행
    //반환 값이 true 일 경우 컨트롤러로 진입하고 false 일 경우 진입  x
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[Request INFO] URL : " + request.getMethod() + " " + request.getRequestURI());
        request.setAttribute(Audit.AUDIT_REQUEST_TIME, System.currentTimeMillis());
        //MDC : 현재 실행중인 스레드에 메타 정보를 넣고 관리하는 공간
        MDC.put(Constant.PROJ_TRANSACTION_ID, applicationName + "-" + UUID.randomUUID());
        return true;
    }

    // 컨트롤러 진입 후 view 가 랜더링 된 후에 실행 (여기선 API 만 있어서 렌더링 x )
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler != null && handler instanceof HandlerMethod) {
            try {
                auditLogService.logAudit((HandlerMethod) handler,request,response,ex);
            }catch (Exception e) {
                log.warn("Error on Logging Audit", e.getMessage());
            } finally {
                MDC.clear(); // 필수로 해줘야함. 스레드별로 MDC가 관리되기 때문에, 스레드가 재사용되면서 남아있을 수 있음.
            }
        }
    }
}
