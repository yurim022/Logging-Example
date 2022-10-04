package com.example.logging.conifg;

import com.example.logging.interceptor.AuditLogInterceptor;
import com.example.logging.service.AuditLogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public AuditLogInterceptor auditLogInterceptor() { return new AuditLogInterceptor(new AuditLogService());}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditLogInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST","PUT","GET","OPTIONS","DELETE")
                .allowedHeaders("*")
                .maxAge(3600L)
                .exposedHeaders("*")
                .allowedOriginPatterns("*");
    }
}
