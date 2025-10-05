package com.unindra.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentCategoryResponse {
    
    private String id;

    private String name;

    private Integer totalPaymentDetails;

}