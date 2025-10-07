package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Classroom;
import com.unindra.entity.Course;
import com.unindra.entity.Department;
import com.unindra.model.request.CourseRequest;
import com.unindra.model.request.CourseResponse;
import com.unindra.model.request.CourseUpdateRequest;
import com.unindra.repository.CourseRepository;
import com.unindra.service.CourseService;
import com.unindra.service.DepartmentService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;

    private final ValidationService validationService;

    private final DepartmentService departmentService;

    @Override
    public List<CourseResponse> getAll() {
        return repository.findAll().stream()
                .map(course -> getCourseResponse(course))
                .toList();

    }

    @Override
    @Transactional
    public void add(CourseRequest request, Locale locale) {
        validationService.validate(request);

        Department department = departmentService.findByName(request.getDepartmentName())
                .orElseThrow(() -> ExceptionUtil.badRequest("department.notfound", locale));

        // cari classroom by name dalam department
        Classroom classroom = department.getClassrooms().stream()
                .filter(c -> c.getName().equals(request.getClassroomName()))
                .findFirst()
                .orElseThrow(() -> ExceptionUtil.badRequest("classroom.notfound", locale));

        // cek apakah course dengan nama sama sudah ada di classroom
        if (repository.existsByClassroomAndName(classroom, request.getName())) {
            throw ExceptionUtil.badRequest("course.name.already.exists", locale);
        }

        String code = generateCode(department.getCode(), classroom);

        Course course = new Course();
        course.setClassroom(classroom);
        course.setCode(code);
        course.setName(request.getName());

        repository.save(course);
    }

    @Override
    @Transactional
    public void update(String id, CourseUpdateRequest request, Locale locale) {
        validationService.validate(request);

        Course course = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.badRequest("course.notfound", locale));

        Classroom classroom = course.getClassroom();

        // cek duplikat nama (exclude course yang sedang diupdate)
        repository.findByClassroomAndName(classroom, request.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(course.getId())) {
                        throw ExceptionUtil.badRequest("course.name.already.exists", locale);
                    }
                });


        course.setName(request.getName());

        repository.save(course);
    }

    @Override
    public void delete(String id, Locale locale) {
        Course course = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.badRequest("course.notfound", locale));

        repository.delete(course);
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

    @Transactional
    public String generateCode(String deptName, Classroom classroom) {
        int max = 0;
        for (Course course : classroom.getCourses()) {
            String[] names = course.getCode().split("-");
            if (Integer.parseInt(names[1]) > max) {
                max = Integer.parseInt(names[1]);
            }
        }
        return String.format("%s%s-%02d", deptName, classroom.getName(), max + 1);
    }

    @Override
    public CourseResponse getByCode(String code, Locale locale) {
        return repository.findByCode(code)
                .map(c -> CourseResponse.builder()
                .id(c.getId())
                .departmentName(c.getClassroom().getDepartment().getName())
                .classroomName(c.getClassroom().getName())
                .code(c.getCode())
                .name(c.getName())
                .build())
            .orElseThrow(() -> ExceptionUtil.badRequest("course.notfound", locale));
    }
}
