package com.unindra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.response.PaymentDetailResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.PaymentDetailService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PaymentDetailController {

    private final PaymentDetailService service;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping(path = "/staff/payment-details")
    public ResponseEntity<WebResponse<List<PaymentDetailResponse>>> get() {
        return ResponseEntity.ok(
                WebResponse.<List<PaymentDetailResponse>>builder()
                        .data(service.getAll())
                        .build());
    }

}