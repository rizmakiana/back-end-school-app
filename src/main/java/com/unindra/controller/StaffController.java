package com.unindra.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.RegisterStaffRequest;
import com.unindra.model.response.StaffResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.StaffService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/staff")
public class StaffController {

    private final StaffService service;

    private final MessageSource source;

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> register(@RequestBody RegisterStaffRequest request, Locale locale) {
        service.register(request, locale);

        WebResponse<String> response = WebResponse.<String>builder()
                .message(source.getMessage("register.success", null, locale))
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/me")
    public ResponseEntity<WebResponse<StaffResponse>> getCurrentStaff(
            Authentication authentication,
            Locale locale) {

        return ResponseEntity.ok(
                WebResponse.<StaffResponse>builder()
                        .data(service.getCurrentStaff(authentication, locale))
                        .build()

        );
    }
}
