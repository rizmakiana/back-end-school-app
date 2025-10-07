package com.unindra.service;

import java.util.List;
import java.util.Locale;

import com.unindra.entity.Section;
import com.unindra.model.request.SectionRequest;
import com.unindra.model.request.SectionUpdateRequest;
import com.unindra.model.response.SectionResponse;

public interface SectionService {
    
    List<SectionResponse> getAll();

    void add(SectionRequest request, Locale locale);

    void update(String id, SectionUpdateRequest request, Locale locale);

    void delete(String id, Locale locale);

    void save(Section section);

    SectionResponse getByCode(String code, Locale locale);
    
}