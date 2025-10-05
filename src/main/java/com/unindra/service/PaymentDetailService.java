package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.PaymentDetailRequest;
import com.unindra.model.response.PaymentDetailResponse;

public interface PaymentDetailService {

    List<PaymentDetailResponse> getAll();

    void add(PaymentDetailRequest request, Locale locale);

    void delete(String id, Locale locale);
    
}
