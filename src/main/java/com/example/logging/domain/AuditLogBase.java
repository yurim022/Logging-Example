package com.example.logging.domain;

import com.example.logging.formatter.AuditLogFormatter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogBase {

    private String timestamp;
    private String service;
    private String operation;
    private String transactionId;
    private String logType;
    private String payload;
    private String url;
    private AuditLogHost host;
    private AuditLogResponse response;
    private AuditLogUser user;
    private String serviceDomain;
    private AuditLogSecurity security;

    @Override
    public String toString() {
        try {
            return AuditLogFormatter.toJsonString(this);
        }catch (Exception e) {
            return super.toString();
        }
    }
}
