package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.entity.Section;
import com.unindra.model.request.DepartmentRequest;
import com.unindra.model.response.DepartmentResponse;
import com.unindra.repository.DepartmentRepository;
import com.unindra.service.DepartmentService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    private final ValidationService validationService;

    @Override
    @Transactional
    public List<DepartmentResponse> getAll() {
        return repository.findAll().stream()
                .map(department -> DepartmentResponse.builder()
                        .id(department.getId())
                        .name(department.getName())
                        .code(department.getCode())
                        .totalClassroom(department.getClassrooms().size())
                        .build())
                .toList();
    }

    @Override
    public void add(DepartmentRequest request, Locale Locale) {
        validationService.validate(request);

        if (isNameExists(request.getName())) {
            throw ExceptionUtil.badRequest("department.name.exists", Locale);
        }

        if (isCodeNameExists(request.getCode())) {
            throw ExceptionUtil.badRequest("department.code.exists", Locale);
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setCode(request.getCode());

        repository.save(department);
    }

    @Override
    @Transactional
    public void update(String id, DepartmentRequest request, Locale locale) {
        validationService.validate(request);

        Department departmentTarget = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.notFound("department.notfound", locale));

        // Cek apakah name sudah dipakai department lain
        repository.findByName(request.getName()).ifPresent(dept -> {
            if (!dept.getId().equals(departmentTarget.getId())) {
                throw ExceptionUtil.badRequest("department.name.exists", locale);
            }
        });

        // Cek apakah code sudah dipakai department lain
        repository.findByCode(request.getCode()).ifPresent(dept -> {
            if (!dept.getId().equals(departmentTarget.getId())) {
                throw ExceptionUtil.badRequest("department.code.exists", locale);
            }
        });

        // Update data
        departmentTarget.setName(request.getName());
        departmentTarget.setCode(request.getCode());

        if (!departmentTarget.getClassrooms().isEmpty()) {
            for (Classroom classroom : departmentTarget.getClassrooms()) {
                String newClassroomCode = request.getCode() + classroom.getName();
                classroom.setCode(newClassroomCode);

                if (!classroom.getSections().isEmpty()) {
                    for (Section section : classroom.getSections()) {
                        String newSectionCode = newClassroomCode + " " + section.getName();
                        section.setCode(newSectionCode);
                    }
                }
            }

        }

        repository.save(departmentTarget);
    }

    @Override
    public void delete(String id, Locale locale) {
        Department dept = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.notFound("department.notFound", locale));

        if (!dept.getClassrooms().isEmpty()) {
            throw ExceptionUtil.badRequest("department.has.classroom", locale);
        }

        repository.delete(dept);
    }

    public boolean isNameExists(String name) {

        return repository.existsByName(name);

    }

    public boolean isCodeNameExists(String code) {

        return repository.existsByCode(code);

    }

    @Override
    public Optional<Department> findByName(String name) {
        System.out.println(repository.existsByName(name));
        return repository.findByName(name);
    }

}
