package com.unindra.service;

import java.util.Locale;

import com.unindra.model.request.LoginRequest;
import com.unindra.model.response.TokenResponse;

public interface AuthService {
    
    TokenResponse loginStaff(LoginRequest request, Locale locale);

}
