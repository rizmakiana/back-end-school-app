package com.unindra.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomResponse {
    
    private String id; 

    private String departmentName;

    private String name;

    private String code;

    private Integer totalSection;

}