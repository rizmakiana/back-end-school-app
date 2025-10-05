package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void delete(String id, Locale locale) {
        PaymentCategory payment = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.badRequest("payment.category.notfound", locale));

        if (!payment.getPaymentDetails().isEmpty()) {
            throw ExceptionUtil.badRequest("payment.detail.exists", locale);
        }

        repository.delete(payment);
    }

    @Override
    public void update(String id, PaymentCategoryRequest request, Locale locale) {

        PaymentCategory payment = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.badRequest("payment.category.notfound", locale));

        repository.findByName(request.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(payment.getId())) {
                throw ExceptionUtil.badRequest("payment.category.name.already.exists", locale);
            }
        });

        payment.setName(request.getName());
        repository.save(payment);
    }

    public PaymentCategoryResponse getResponse(PaymentCategory payment) {
        return PaymentCategoryResponse.builder()
                .id(payment.getId())
                .name(payment.getName())
                .totalPaymentDetails(payment.getPaymentDetails().size())
                .build();
    }

}
