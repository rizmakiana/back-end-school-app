package com.unindra.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentRequest {
    
    @NotBlank(message = "{department.name.notblank}")
    private String name;
    
    @NotBlank(message = "{department.code.notblank}")
    private String code;
}
