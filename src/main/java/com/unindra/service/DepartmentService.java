package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.DepartmentRequest;
import com.unindra.model.response.DepartmentResponse;

public interface DepartmentService {
    
    List<DepartmentResponse> getAll();

    void add(DepartmentRequest request, Locale Locale);

    void update(String id, DepartmentRequest request, Locale Locale);

    void delete(String id, Locale locale);

}