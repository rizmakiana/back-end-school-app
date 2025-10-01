package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.entity.Section;
import com.unindra.model.request.SectionRequest;
import com.unindra.model.response.SectionResponse;
import com.unindra.repository.SectionRepository;
import com.unindra.service.ClassroomService;
import com.unindra.service.DepartmentService;
import com.unindra.service.SectionService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionRepository repository;

    private final DepartmentService departmentService;

    private final ClassroomService classroomService;

    private final ValidationService validationService;

    @Override
    public List<SectionResponse> getAll() {
        return repository.findAll().stream()
                .map(section -> SectionResponse.builder()
                        .id(section.getId())
                        .departmentName(section.getClassroom().getDepartment().getName())
                        .classroomName(section.getClassroom().getName())
                        .name(section.getName())
                        .code(section.getCode())
                        .totalStudent(section.getStudents().size())
                        .build())
                .toList();
    }

    @Override
    public void add(SectionRequest request, Locale locale) {
        validationService.validate(request);

        Department department = departmentService.findByName(request.getDepartmentName())
                .orElseThrow(() -> ExceptionUtil.badRequest("department.notfound", locale));

        Classroom classroom = classroomService.findByDepartmentAndName(department, request.getClassroomName())
                .orElseThrow(() -> ExceptionUtil.badRequest("classroom.notfound", locale));

        Character name = getSectionNameList(department, classroom.getName());
        Section section = new Section();
        section.setClassroom(classroom);
        section.setName(name);
        section.setCode(String.format("%s %s", classroom.getCode(), name));

        repository.save(section);

    }

    @Override
    public void update(String id, SectionRequest request, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Transactional(readOnly = true)
    public Character getSectionNameList(Department department, String classroomName) {
        Classroom classroom = department.getClassrooms().stream()
                .filter(c -> c.getName().equals(classroomName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

        char last = 'A' - 1; // supaya kalau belum ada section, hasilnya 'A'
        for (Section section : classroom.getSections()) {
            if (section.getName() > last) {
                last = section.getName();
            }
        }

        return (char) (last + 1); // next huruf
    }

}
