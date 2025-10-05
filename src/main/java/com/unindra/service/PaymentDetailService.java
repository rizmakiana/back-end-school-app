package com.unindra.service;

import java.util.List;

import com.unindra.model.response.PaymentDetailResponse;

public interface PaymentDetailService {

    List<PaymentDetailResponse> getAll();
    
}
