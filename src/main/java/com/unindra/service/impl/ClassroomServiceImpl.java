package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.model.request.ClassroomRequest;
import com.unindra.model.response.ClassroomResponse;
import com.unindra.repository.ClassroomRepository;
import com.unindra.service.ClassroomService;
import com.unindra.service.DepartmentService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository repository;

    private final ValidationService validationService;

    private final DepartmentService departmentService;

    @Override
    public List<ClassroomResponse> getAll() {
        return repository.findAll().stream()
                .map(classroom -> ClassroomResponse.builder()
                        .id(classroom.getId())
                        .departmentName(classroom.getDepartment().getName())
                        .name(classroom.getName())
                        .code(classroom.getCode())
                        .totalSection(classroom.getSections().size())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void add(ClassroomRequest request, Locale locale) {
        validationService.validate(request);

        Department d = departmentService.findByName(request.getDepartmentName())
                .orElseThrow(() -> ExceptionUtil.badRequest("department.notfound", locale));

        if (repository.existsByDepartmentAndName(d, request.getClassroomName())) {
            throw ExceptionUtil.badRequest("classroom.already.exists", locale);
        }

        Classroom classroom = new Classroom();
        classroom.setDepartment(d);
        classroom.setName(request.getClassroomName());
        classroom.setCode(d.getCode() + request.getClassroomName());

        repository.save(classroom);
    }

    @Override
    public void delete(String id, Locale locale) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.notFound("classroom.notfound", locale));

        if (!classroom.getSections().isEmpty()) {
            throw ExceptionUtil.badRequest("classroom.cant.delete", locale);
        }

        repository.delete(classroom);
    }

    @Override
    public Optional<Classroom> findByDepartmentAndName(Department department, String name) {
        return repository.findByDepartmentAndName(department, name);
    }

    @Override
    public void save(Classroom classroom) {
        repository.save(classroom);
    }

    @Override
    public Optional<Classroom> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public ClassroomResponse getByCode(String code, Locale locale) {
        return repository.findByCode(code)
                .map(classroom -> ClassroomResponse.builder()
                        .id(classroom.getId())
                        .departmentName(classroom.getDepartment().getName())
                        .name(classroom.getName())
                        .totalSection(classroom.getSections().size())
                        .build())
                .orElseThrow(() -> ExceptionUtil.badRequest("classroom.notfound", locale));
    }

}
