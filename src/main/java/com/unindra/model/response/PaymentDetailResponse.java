package com.unindra.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetailResponse {

    private String id;

    private String categoryName;

    private String classroomName;

    private String name;

    private String amount;
    
}