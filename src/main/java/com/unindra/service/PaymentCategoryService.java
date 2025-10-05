package com.unindra.service;

import java.util.Locale;

import com.unindra.model.request.PaymentCategoryRequest;

public interface PaymentCategoryService {

    void add(PaymentCategoryRequest request, Locale locale);

}
