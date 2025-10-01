package com.unindra.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomRequest {

    private String departmentName;

    private String classroomName;

    private String code;

    private Integer totalSection;
    
}