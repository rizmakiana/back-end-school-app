package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Course;
import com.unindra.model.request.CourseRequest;
import com.unindra.model.request.CourseResponse;
import com.unindra.repository.CourseRepository;
import com.unindra.service.CourseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;

    @Override
    public List<CourseResponse> getAll() {
        return repository.findAll().stream()
            .map(course -> getCourseResponse(course))
            .toList();

    }

    @Override
    public void add(CourseRequest request, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void update(String id, CourseRequest request, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(String id, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .departmentName(course.getClassroom().getDepartment().getName())
                .classroomName(course.getClassroom().getName())
                .name(course.getName())
                .build();
    }
}
