package com.unindra.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.LoginRequest;
import com.unindra.model.response.TokenResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final MessageSource source;

    @PostMapping(path = "/login/staff", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<TokenResponse>> loginStaff(@RequestBody LoginRequest request, Locale locale) {
        return ResponseEntity
                .ok(WebResponse.<TokenResponse>builder()
                        .data(authService.loginStaff(request, locale))
                        .message(source.getMessage("login.success", null, locale))
                        .build());
    }
}
