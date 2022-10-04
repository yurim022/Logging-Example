package com.example.logging.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogUser {
    private String id;
    private String type;
    private String ip;
    private String agent;
}
