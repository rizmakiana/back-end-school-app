package com.unindra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.PaymentDetailRequest;
import com.unindra.model.response.PaymentDetailResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.PaymentDetailService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PaymentDetailController {

    private final PaymentDetailService service;

    private final MessageSource source;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping(path = "/staff/payment-details")
    public ResponseEntity<WebResponse<List<PaymentDetailResponse>>> get() {
        return ResponseEntity.ok(
                WebResponse.<List<PaymentDetailResponse>>builder()
                        .data(service.getAll())
                        .build());
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(path = "/staff/payment-details")
    public ResponseEntity<WebResponse<String>> add(@RequestBody PaymentDetailRequest request, Locale locale) {
        return ResponseEntity.ok(
            WebResponse.<String>builder()
            .message(source.getMessage("payment.detail.added.succesfully", null, locale))
            .build());
            
        }
        
    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping(path = "/staff/payment-details/{id}")
    public void delete(@PathVariable String id, Locale locale) {
        ResponseEntity.ok(
            WebResponse.<String>builder()
            .message(source.getMessage("payment.detail.deleted", null, locale))
            .build()
        );
        
    }
}