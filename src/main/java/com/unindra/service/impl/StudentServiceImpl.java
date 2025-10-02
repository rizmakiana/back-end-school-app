package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.unindra.entity.Student;
import com.unindra.model.request.StudentRequest;
import com.unindra.model.response.StudentResponse;
import com.unindra.repository.StudentRepository;
import com.unindra.service.StudentService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    @Override
    public List<StudentResponse> getAll() {
        return repository.findAll().stream()
                .map(student -> generateStudentResponse(student))
                .toList();
    }

    @Override
    public void add(StudentRequest request, Locale Locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void update(String id, StudentRequest request, Locale Locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(String id, Locale locale) {
        Student student = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.badRequest("student.notfound", locale));
        
        repository.delete(student);
    }

    public StudentResponse generateStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .gender(student.getGender())
                .birthPlace(student.getBirthplace().getId())
                .birthDate(student.getBirthDate())
                .districtAddress(student.getDistrictAddress().getId())
                .address(student.getAddress())
                .username(student.getUsername())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .build();
    }
}
