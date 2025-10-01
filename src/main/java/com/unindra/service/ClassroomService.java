package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.ClassroomRequest;
import com.unindra.model.response.ClassroomResponse;

public interface ClassroomService {
    
    List<ClassroomResponse> getAll();

    void add(ClassroomRequest request, Locale locale);

    void delete(String id, Locale locale);

}
