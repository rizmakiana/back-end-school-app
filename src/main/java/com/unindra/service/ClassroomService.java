package com.unindra.service;

import java.util.Locale;

import com.unindra.model.request.ClassroomRequest;

public interface ClassroomService {
    
    void get();
    
    void getAll();

    void add(ClassroomRequest request, Locale locale);

}
