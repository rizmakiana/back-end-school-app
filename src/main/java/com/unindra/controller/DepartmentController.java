package com.unindra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.response.DepartmentResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.DepartmentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/staff/departments")
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping
    public ResponseEntity<WebResponse<List<DepartmentResponse>>> getAll(Authentication authentication) {

        WebResponse<List<DepartmentResponse>> response = WebResponse.<List<DepartmentResponse>>builder()
                .data(service.getAll())
                .build();

        return ResponseEntity.ok(response);
    }
}
