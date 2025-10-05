package com.unindra.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unindra.entity.Classroom;
import com.unindra.entity.PaymentDetail;
import com.unindra.model.response.PaymentDetailResponse;
import com.unindra.repository.PaymentDetailRepository;
import com.unindra.service.PaymentDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentDetailServiceImpl implements PaymentDetailService{
    
    private final PaymentDetailRepository repository;

    @Override
    public List<PaymentDetailResponse> getAll() {
        return repository.findAll().stream()
            .map(payment -> getResponse(payment))
            .toList();
    }

    public PaymentDetailResponse getResponse(PaymentDetail detail) {
        return PaymentDetailResponse.builder()
            .id(detail.getId())
            .categoryName(detail.getPaymentCategory().getName())
            .classroomName(Optional.ofNullable(detail.getClassroom()).map(Classroom::getCode).orElse(null))
            .name(detail.getName())
            .amount(detail.getAmount().toPlainString())
            .build();
    }

    
}
