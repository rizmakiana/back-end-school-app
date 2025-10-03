package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.StudentRequest;
import com.unindra.model.response.StudentResponse;

public interface StudentService {
    
    List<StudentResponse> getAll();

    void add(StudentRequest request, Locale Locale);
    
    void update(String id, StudentRequest request, Locale locale);

    void delete(String id, Locale locale);

}