package com.unindra.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Teacher;
import com.unindra.model.request.TeacherRequest;
import com.unindra.model.request.TeacherUpdate;
import com.unindra.model.response.CourseHandled;
import com.unindra.model.response.TeacherResponse;
import com.unindra.repository.TeacherRepository;
import com.unindra.service.TeacherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {
        return repository.findAll().stream()
            .map(t -> getTeacherResponse(t)).toList();
    }

    @Override
    public void add(TeacherRequest request, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void update(String id, TeacherUpdate request, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(String id, Locale locale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public TeacherResponse getTeacherResponse(Teacher teacher) {
        return TeacherResponse.builder()
            .id(teacher.getId())
            .name(teacher.getName())
            .gender(teacher.getGender())
            .birthPlace(teacher.getBirthplace().getId())
            .birthDate(teacher.getBirthDate())
            .districtAddress(teacher.getDistrictAddress().getId())
            .address(teacher.getAddress())
            .username(teacher.getUsername())
            .email(teacher.getEmail())
            .phoneNumber(teacher.getPhoneNumber())
            .courseHandleds(getCourseHandled(teacher))
            .build();
    }

    public List<CourseHandled> getCourseHandled(Teacher teacher) {
        return teacher.getTeachingAssignments().stream()
                .map(t -> CourseHandled.builder()
                        .courseName(t.getCourse().getCode())
                        .sectionCode(t.getSection().getCode()).build())
                .toList();

    }

}
