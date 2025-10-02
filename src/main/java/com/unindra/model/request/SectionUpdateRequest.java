package com.unindra.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionUpdateRequest {
    
    @NotBlank(message = "{section.code.notblank}")
    @Size(min = 1, max = 1, message = "{section.code.size}")
    private String code;

}
