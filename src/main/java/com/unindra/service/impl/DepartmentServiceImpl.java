package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.model.request.DepartmentRequest;
import com.unindra.model.response.DepartmentResponse;
import com.unindra.repository.DepartmentRepository;
import com.unindra.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    @Override
    @Transactional
    public List<DepartmentResponse> getAll() {
        return repository.findAll().stream()
                .map(department -> DepartmentResponse.builder()
                        .id(department.getId())
                        .name(department.getName())
                        .code(department.getCode())
                        .totalClassroom(String.valueOf(department.getClassrooms().size()))
                        .build())
                .toList();
    }

    @Override
    public void add(DepartmentRequest request, Locale Locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }
    

    @Override
    public void update(DepartmentRequest request, Locale Locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public boolean isNameExists(String name){
        
        return repository.existsByName(name);

    }

    public boolean isCodeNameExists(String code) {
        
        return repository.existsByCode(code);

    }
    
}
