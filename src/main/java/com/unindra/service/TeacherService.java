package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.TeacherRequest;
import com.unindra.model.request.TeacherUpdate;
import com.unindra.model.response.TeacherResponse;

public interface TeacherService {

    List<TeacherResponse> getAll();

    void add(TeacherRequest request, Locale locale);

    void update(String id, TeacherUpdate request, Locale locale);

    void delete(String id, Locale locale);
    
}
