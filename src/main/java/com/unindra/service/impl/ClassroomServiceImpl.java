package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.unindra.model.request.ClassroomRequest;
import com.unindra.model.response.ClassroomResponse;
import com.unindra.repository.ClassroomRepository;
import com.unindra.service.ClassroomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository repository;

    @Override
    public List<ClassroomResponse> getAll() {
        return repository.findAll().stream()
                .map(classroom -> ClassroomResponse.builder()
                        .id(classroom.getId())
                        .departmentName(classroom.getDepartment().getName())
                        .name(classroom.getName())
                        .totalSection(classroom.getSections().size())
                        .build())
                .toList();
    }

    @Override
    public void add(ClassroomRequest request, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
