package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.unindra.model.request.SectionRequest;
import com.unindra.model.response.SectionResponse;
import com.unindra.repository.SectionRepository;
import com.unindra.service.SectionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionRepository repository;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
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

}
