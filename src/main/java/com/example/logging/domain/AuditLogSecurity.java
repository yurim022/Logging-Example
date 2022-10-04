package com.example.logging.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogSecurity {

    private String type;
    private String event;
    private String target;
    private String detail;
    private String reason;
    private String personalInfoList;
}
