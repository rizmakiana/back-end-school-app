package com.unindra.service.impl;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.unindra.entity.Staff;
import com.unindra.model.request.LoginRequest;
import com.unindra.model.response.TokenResponse;
import com.unindra.security.BCrypt;
import com.unindra.service.AuthService;
import com.unindra.service.StaffService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;
import com.unindra.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ValidationService validationService;

    private final StaffService staffService;

    private final JwtUtil jwtUtil;

    @Override
    public TokenResponse loginStaff(LoginRequest request, Locale locale) {
        System.out.println("DB_NAME = " + System.getenv("DB_NAME"));

        validationService.validate(request);

        Staff staff = staffService.findAccount(request.getUsername())
                .orElseThrow(() -> ExceptionUtil.badRequest("login.fail", locale));
        
        boolean isPasswordMatch = BCrypt.checkpw(request.getPassword(), staff.getPassword());
        if (!isPasswordMatch) {
            throw ExceptionUtil.badRequest("login.fail", locale);
        }

        return jwtUtil.generateToken(staff);
    }

}
