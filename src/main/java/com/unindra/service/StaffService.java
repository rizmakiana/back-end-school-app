package com.unindra.service;

import java.util.Locale;
import java.util.Optional;

import com.unindra.entity.Staff;
import com.unindra.model.request.RegisterStaffRequest;

public interface StaffService {

    void register(RegisterStaffRequest request, Locale locale);
    
    Optional<Staff> findAccount(String args);
}