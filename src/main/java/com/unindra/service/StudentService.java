package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.StudentRequest;
import com.unindra.model.request.StudentUpdate;
import com.unindra.model.response.StudentResponse;

public interface StudentService {
    
    List<StudentResponse> getAll();

    void add(StudentRequest request, Locale locale);
    
    void update(String id, StudentUpdate request, Locale locale);

    void delete(String id, Locale locale);

}