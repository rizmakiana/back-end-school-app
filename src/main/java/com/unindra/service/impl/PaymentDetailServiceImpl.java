package com.unindra.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unindra.entity.Classroom;
import com.unindra.entity.PaymentCategory;
import com.unindra.entity.PaymentDetail;
import com.unindra.model.request.PaymentDetailRequest;
import com.unindra.model.response.PaymentDetailResponse;
import com.unindra.repository.PaymentDetailRepository;
import com.unindra.service.ClassroomService;
import com.unindra.service.PaymentCategoryService;
import com.unindra.service.PaymentDetailService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentDetailServiceImpl implements PaymentDetailService {

    private final PaymentDetailRepository repository;

    private final ValidationService validationService;

    private final PaymentCategoryService paymentCategoryService;

    private final ClassroomService classroomService;

    @Override
    public List<PaymentDetailResponse> getAll() {
        return repository.findAll().stream()
                .map(payment -> getResponse(payment))
                .toList();
    }

    @Override
    public void add(PaymentDetailRequest request, Locale locale) {
        validationService.validate(request);

        PaymentCategory category = paymentCategoryService.findByName(request.getCategory())
                .orElseThrow(() -> ExceptionUtil.badRequest("payment.category.notfound", locale));

        Classroom classroom = classroomService.findByCode(request.getClassroomCode()).orElse(null);

        if (repository.existsByPaymentCategoryAndClassroomAndName(category, classroom, request.getName())) {
            throw ExceptionUtil.badRequest("payment.detail.already.exists", locale);
        }

        BigDecimal amount = Optional.ofNullable(request.getAmount())
                .map(a -> {
                    try {
                        return new BigDecimal(a);
                    } catch (NumberFormatException e) {
                        throw ExceptionUtil.badRequest("payment.amount.not.valid", locale);
                    }
                })
                .filter(a -> a.compareTo(BigDecimal.ZERO) > 0)
                .orElseThrow(() -> ExceptionUtil.badRequest("payment.amount.must.be.positive", locale));

        PaymentDetail detail = new PaymentDetail();

        detail.setPaymentCategory(category);
        detail.setClassroom(classroom);
        detail.setName(request.getName());
        detail.setAmount(amount);

        repository.save(detail);
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

    @Override
    public void delete(String id, Locale locale) {
        PaymentDetail detail = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.badRequest("payment.detail.not.found", locale));

        if (!detail.getPayments().isEmpty()) {
            throw ExceptionUtil.badRequest("payment.detail.cannot.delete.has.payments", locale);
        }

        repository.delete(detail);
    }

}