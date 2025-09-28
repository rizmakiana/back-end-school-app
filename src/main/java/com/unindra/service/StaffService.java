package com.unindra.service;

import java.util.Locale;

import com.unindra.model.request.RegisterStaffRequest;

public interface StaffService {

    void register(RegisterStaffRequest request, Locale locale);
    
}