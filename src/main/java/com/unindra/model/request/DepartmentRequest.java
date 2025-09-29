package com.unindra.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentRequest {
    
    private String name;

    private String code;
}
