package com.example.logging.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogHost {

    private String name;
    private String ip;

}
