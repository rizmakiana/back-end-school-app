package com.unindra.service.impl;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.District;
import com.unindra.entity.Regency;
import com.unindra.entity.Teacher;
import com.unindra.model.request.TeacherRequest;
import com.unindra.model.request.TeacherUpdate;
import com.unindra.model.response.CourseHandled;
import com.unindra.model.response.TeacherResponse;
import com.unindra.repository.TeacherRepository;
import com.unindra.service.DistrictService;
import com.unindra.service.RegencyService;
import com.unindra.service.TeacherService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;

    private final ValidationService validationService;

    private final RegencyService regencyService;

    private final DistrictService districtService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {
        return repository.findAll().stream()
                .map(t -> getTeacherResponse(t)).toList();
    }

    @Override
    public void add(TeacherRequest request, Locale locale) {
        validationService.validate(request);

        boolean isPasswordEquals = request.getPassword().equals(request.getConfirmPassword());
        if (!isPasswordEquals) {
            throw ExceptionUtil.badRequest("password.not.equals", locale);
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(
                    Integer.parseInt(request.getBirthYear()),
                    request.getBirthMonth(),
                    Integer.parseInt(request.getBirthDate()));
        } catch (DateTimeException | NumberFormatException e) {
            throw ExceptionUtil.badRequest("invalid.date", locale);
        }

        Regency regency = regencyService.findById(request.getBirthPlaceRegency())
                .orElseThrow(() -> ExceptionUtil.notFound("regency.notfound", locale));

        District district = districtService.findById(request.getDistrictAddress())
                .orElseThrow(() -> ExceptionUtil.notFound("district.notfound", locale));

        if (existsByUsername(request.getUsername())) {
            throw ExceptionUtil.badRequest("username.already.exists", locale);
        }

        if (existsByEmail(request.getEmail())) {
            throw ExceptionUtil.badRequest("email.already.exists", locale);
        }

        if (existsByPhoneNumber(request.getPhoneNumber())) {
            throw ExceptionUtil.badRequest("phone.number.already.exists", locale);
        }

        Teacher teacher = new Teacher();

        teacher.setName(request.getName());
        teacher.setGender(request.getGender());
        teacher.setBirthplace(regency);
        teacher.setBirthDate(birthDate);
        teacher.setDistrictAddress(district);
        teacher.setAddress(request.getAddress());
        teacher.setUsername(request.getUsername());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setEmail(request.getEmail());
        teacher.setPhoneNumber(request.getPhoneNumber());

        repository.save(teacher);
    }

    @Override
    public void update(String id, TeacherUpdate request, Locale locale) {
        validationService.validate(request);

        Teacher teacher = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.badRequest("teacher.not.found", locale));
        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(
                    Integer.parseInt(request.getBirthDate()),
                    request.getBirthMonth(),
                    Integer.parseInt(request.getBirthDate()));
        } catch (DateTimeException | NumberFormatException e) {
            throw ExceptionUtil.badRequest("invalid.date", locale);
        }

        Regency regency = regencyService.findById(request.getBirthPlaceRegency())
                .orElseThrow(() -> ExceptionUtil.notFound("regency.notfound", locale));

        District district = districtService.findById(request.getDistrictAddress())
                .orElseThrow(() -> ExceptionUtil.notFound("district.notfound", locale));

        repository.findByUsername(request.getUsername())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> {
                    throw ExceptionUtil.badRequest("username.already.exists", locale);
                });

        repository.findByEmail(request.getEmail())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> {
                    throw ExceptionUtil.badRequest("email.already.exists", locale);
                });

        repository.findByPhoneNumber(request.getPhoneNumber())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> {
                    throw ExceptionUtil.badRequest("phone.number.already.exists", locale);
                });

        teacher.setName(request.getName());
        teacher.setGender(request.getGender());
        teacher.setBirthplace(regency);
        teacher.setBirthDate(birthDate);
        teacher.setDistrictAddress(district);
        teacher.setAddress(request.getAddress());
        teacher.setUsername(request.getUsername());
        teacher.setEmail(request.getEmail());
        teacher.setPhoneNumber(request.getPhoneNumber());

        repository.save(teacher);
    }

    @Override
    public void delete(String id, Locale locale) {

        Teacher teacher = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.notFound("teacher.not.found", locale));

        boolean hasAssignments = !teacher.getTeachingAssignments().isEmpty();

        if (hasAssignments) {
            boolean hasActivity = teacher.getTeachingAssignments().stream()
                    .anyMatch(ta -> !ta.getMeetingNumbers().isEmpty());

            if (hasActivity) {
                throw ExceptionUtil.badRequest("teacher.cannot.be.deleted.with.existing.records", locale);
            }
        }

        repository.delete(teacher);
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

    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

}
