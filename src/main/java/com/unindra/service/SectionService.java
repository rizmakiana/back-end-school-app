package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.model.request.SectionRequest;
import com.unindra.model.response.SectionResponse;

public interface SectionService {
    
    List<SectionResponse> getAll();

    void add(SectionRequest request, Locale locale);

    void update(String id, SectionRequest request, Locale locale);

    void delete(String id, Locale locale);
    
}