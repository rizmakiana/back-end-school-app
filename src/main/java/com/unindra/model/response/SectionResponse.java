package com.unindra.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {
    
    private String id;

    private String departmentName;

    private String classroomName;

    private Character name;

    private String code;

    private Integer totalStudent;

}