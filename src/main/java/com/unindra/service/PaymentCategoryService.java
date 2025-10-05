package com.unindra.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.unindra.entity.PaymentCategory;
import com.unindra.model.request.PaymentCategoryRequest;
import com.unindra.model.response.PaymentCategoryResponse;

public interface PaymentCategoryService {

    void add(PaymentCategoryRequest request, Locale locale);

    List<PaymentCategoryResponse> getAll();

    void delete(String id, Locale locale);

    void update(String id, PaymentCategoryRequest request, Locale locale);

    Optional<PaymentCategory> findByName(String name);

}
