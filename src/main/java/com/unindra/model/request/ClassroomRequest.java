package com.unindra.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomRequest {

    @NotBlank(message = "{department.name.notblank}")
    private String departmentName;

    @NotBlank(message = "{classroom.name.notblank}")
    private String classroomName;

    @NotBlank(message = "{classroom.code.notblank}")
    private String code;

    
}