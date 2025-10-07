package com.unindra.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseUpdateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\d{1,2}$")
    private String code;

}