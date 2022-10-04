package com.example.logging.annotation;

import com.example.logging.conifg.AuditLogConfig;
import com.example.logging.domain.AuditEnum;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import({AuditLogConfig.class})
@Documented
public @interface Audit {

    String AUDIT_REQUEST_TIME = "audit-request-time";
    String AUDIT_REQUEST_TARGET = "audit-request-target";
    String AUDIT_LOGIN_USER_ID = "audit-login-user-id";
    String AUDIT_REQUEST_DETAIL = "audit-request-detail";

    AuditEnum.Type type();
    AuditEnum.Event event();
    String detail() default "";
    String reason() default "";
    String personalInfoList() default "";

}
