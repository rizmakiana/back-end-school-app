package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.CourseRequest;
import com.unindra.model.request.CourseResponse;

public interface CourseService {

    List<CourseResponse> getAll();
    
    void add(CourseRequest request, Locale locale);

    void update(String id, CourseRequest request, Locale locale);

    void delete(String id, Locale locale);
}
