package com.unindra.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.model.request.ClassroomRequest;
import com.unindra.model.response.ClassroomResponse;

public interface ClassroomService {
    
    List<ClassroomResponse> getAll();

    void add(ClassroomRequest request, Locale locale);

    void delete(String id, Locale locale);

    Optional<Classroom> findByDepartmentAndName(Department department, String name);

    void save(Classroom classroom);
    
}
