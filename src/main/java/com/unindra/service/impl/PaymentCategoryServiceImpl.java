package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.unindra.entity.PaymentCategory;
import com.unindra.model.request.PaymentCategoryRequest;
import com.unindra.model.response.PaymentCategoryResponse;
import com.unindra.repository.PaymentCategoryRepository;
import com.unindra.service.PaymentCategoryService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentCategoryServiceImpl implements PaymentCategoryService {

    private final PaymentCategoryRepository repository;

    private final ValidationService validationService;

    @Override
    public void add(PaymentCategoryRequest request, Locale locale) {
        validationService.validate(request);

        if (repository.existsByName(request.getName())) {
            throw ExceptionUtil.badRequest("payment.category.already.exists", locale);
        }

        PaymentCategory payment = new PaymentCategory();
        payment.setName(request.getName());

        repository.save(payment);
    }

    @Override
    public List<PaymentCategoryResponse> getAll() {
        return repository.findAll().stream()
                .map(payment -> getResponse(payment))
                .toList();
    }

    public PaymentCategoryResponse getResponse(PaymentCategory payment) {
        return PaymentCategoryResponse.builder()
                .id(payment.getId())
                .name(payment.getName())
                .totalPaymentDetails(payment.getPaymentDetails().size())
                .build();
    }

}
