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
            throw ExceptionUtil.badRequest("course.already.exists", locale);
        }

        // hitung jumlah course di classroom untuk generate code
        long count = repository.countByClassroom(classroom);
        String code = generateCode(department, classroom, count);

        Course course = new Course();
        course.setClassroom(classroom);
        course.setCode(code);
        course.setName(request.getName());

        repository.save(course);
    }

    private String generateCode(Department department, Classroom classroom, long currentCount) {
        // contoh: MIPA10-1, MIPA10-2, dst.
        return String.format("%s%s-%d",
                department.getCode(),
                classroom.getName(),
                currentCount + 1);
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

    @Transactional
    public String generateCode(Department department, Classroom classroom) {
        // example format code: MIPA10-1, MIPA10-2, MIPA10-3
        return String.format("%s%s-%d", department.getCode(), classroom.getName(), classroom.getCourses().size() + 1);
    }
}
