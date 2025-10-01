package com.unindra.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionRequest {
 
    @NotBlank(message = "department.not.notblank")
    private String departmentName;

    @NotBlank(message = "classroom.name.notblank")
    private String classroomName;

}