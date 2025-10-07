package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.entity.Section;
import com.unindra.model.request.SectionRequest;
import com.unindra.model.request.SectionUpdateRequest;
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

        Character name = getSectionNameList(classroom, locale);
        Section section = new Section();
        section.setClassroom(classroom);
        section.setName(name);
        section.setCode(String.format("%s %s", classroom.getCode(), name));

        repository.save(section);

    }

    @Override
    public void update(String id, SectionUpdateRequest request, Locale locale) {
        validationService.validate(request);

        Section section = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.notFound("section.notfound", locale));

        // contoh: "MIPA10 A"
        String oldCode = section.getCode();

        // ambil prefix sebelum spasi terakhir → "MIPA10"
        String prefix = oldCode.substring(0, oldCode.lastIndexOf(" "));

        // gabung prefix dengan code baru dari request → "MIPA10 B"
        String name = prefix + " " + request.getCode();

        if (repository.existsByCode(name)) {
            throw ExceptionUtil.badRequest("section.already.exists", locale);
        }

        section.setName(request.getCode().charAt(0));
        section.setCode(name);
        repository.save(section);
    }

    @Override
    public void delete(String id, Locale locale) {
        Section section = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.notFound("section.notfound", locale));

        if (!section.getStudents().isEmpty()) {
            throw ExceptionUtil.badRequest("section.cant.deleted", locale);
        }

        repository.delete(section);
    }

    @Transactional(readOnly = true)
    public Character getSectionNameList(Classroom classroom, Locale locale) {
        char last = 'A' - 1; // supaya kalau belum ada section, hasilnya 'A'
        for (Section section : classroom.getSections()) {
            if (section.getName() > last) {
                last = section.getName();
            }
        }
        if (last == 'Z') {
            throw ExceptionUtil.badRequest("section.max", locale);
        }

        return (char) (last + 1); // next huruf
    }

    @Override
    public void save(Section section) {
        repository.save(section);
    }

    @Override
    public SectionResponse getByCode(String code, Locale locale) {
        return repository.findByCode(code)
                .map(section -> SectionResponse.builder()
                        .id(section.getId())
                        .departmentName(section.getClassroom().getDepartment().getName())
                        .classroomName(section.getClassroom().getName())
                        .name(section.getName())
                        .code(section.getCode())
                        .totalStudent(section.getStudents().size())
                        .build())
                .orElseThrow(() -> ExceptionUtil.notFound("section.notfound", locale));
    }

}
