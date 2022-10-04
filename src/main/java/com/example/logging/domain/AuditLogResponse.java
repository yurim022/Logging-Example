package com.example.logging.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogResponse {

    private String type;
    private String code;
    private String desc;
    private String duration;
}
