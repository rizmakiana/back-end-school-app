package com.unindra.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.PaymentCategoryRequest;
import com.unindra.model.response.WebResponse;
import com.unindra.service.PaymentCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PaymentCategoryController {

    private final PaymentCategoryService service;

    private final MessageSource source;

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(path = "staff/payment-category")
    public ResponseEntity<WebResponse<String>> add(
            @RequestBody PaymentCategoryRequest request,
            Locale locale) {

        service.add(request, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("payment.category.created", null, locale))
                        .build());

    }
}
