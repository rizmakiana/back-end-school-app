package com.unindra.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetailRequest {

    @NotBlank
    private String category;

    private String classroomCode;

    @NotBlank
    private String name;

    @NotBlank
    private String amount;
}
