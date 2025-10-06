package com.unindra.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.unindra.entity.Staff;
import com.unindra.model.request.RegisterStaffRequest;
import com.unindra.model.response.StaffResponse;

public interface StaffService {

    void register(RegisterStaffRequest request, Locale locale);
    
    Optional<Staff> findAccount(String args);

    StaffResponse getCurrentStaff(Authentication authentication, Locale locale);
}