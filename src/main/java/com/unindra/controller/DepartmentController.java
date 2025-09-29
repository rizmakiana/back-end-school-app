package com.unindra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.DepartmentRequest;
import com.unindra.model.response.DepartmentResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.DepartmentService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/staff/departments")
public class DepartmentController {

    private final DepartmentService service;

    private final MessageSource source;

    @GetMapping
    public ResponseEntity<WebResponse<List<DepartmentResponse>>> getAll(Authentication authentication) {

        WebResponse<List<DepartmentResponse>> response = WebResponse.<List<DepartmentResponse>>builder()
                .data(service.getAll())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<WebResponse<String>> create(
            @RequestBody DepartmentRequest request,
            Authentication authentication,
            Locale locale) {

        service.add(request, locale);
        return ResponseEntity.ok(WebResponse.<String>builder()
                .message(source.getMessage("department.created", null, locale)).build());
    }

}
