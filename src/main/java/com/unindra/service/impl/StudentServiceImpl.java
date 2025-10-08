package com.unindra.service.impl;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.entity.District;
import com.unindra.entity.Regency;
import com.unindra.entity.Section;
import com.unindra.entity.Student;
import com.unindra.model.request.StudentRequest;
import com.unindra.model.request.StudentUpdate;
import com.unindra.model.response.StudentResponseTable;
import com.unindra.model.util.Role;
import com.unindra.repository.StudentRepository;
import com.unindra.service.DepartmentService;
import com.unindra.service.DistrictService;
import com.unindra.service.RegencyService;
import com.unindra.service.StudentService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;
import com.unindra.util.TimeFormat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    private final ValidationService validationService;

    private final RegencyService regencyService;

    private final DistrictService districtService;

    private final DepartmentService departmentService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<StudentResponseTable> getAll() {
        return repository.findAll().stream()
                .map(student -> generateStudentResponseTable(student))
                .toList();
    }

    @Override
    @Transactional
    public void add(StudentRequest request, Locale locale) {
        validationService.validate(request);

        System.out.println("STARTTT");
        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(
                    Integer.parseInt(request.getBirthYear()),
                    request.getBirthMonth(),
                    Integer.parseInt(request.getBirthDate()));
        } catch (DateTimeException | NumberFormatException e) {
            throw ExceptionUtil.badRequest("invalid.date", locale);
        }

        log.info("Regency ID : {}", request.getBirthPlaceRegency());
        Regency regency = regencyService.findById(request.getBirthPlaceRegency())
        .orElseThrow(() -> ExceptionUtil.badRequest("regency.notfound", locale));
        
        log.info("District ID : {}", request.getDistrictAddress());
        District district = districtService.findById(request.getDistrictAddress())
                .orElseThrow(() -> ExceptionUtil.badRequest("district.notfound", locale));

        if (existsByUsername(request.getUsername())) {
            throw ExceptionUtil.badRequest("username.already.exists", locale);
        }

        if (existsByEmail(request.getEmail())) {
            throw ExceptionUtil.badRequest("email.already.exists", locale);
        }

        if (existsByPhoneNumber(request.getPhoneNumber())) {
            throw ExceptionUtil.badRequest("phone.number.already.exists", locale);
        }

        Department department = departmentService.findByName(request.getDepartmentName())
                .orElseThrow(() -> ExceptionUtil.badRequest("department.notfound", locale));

        Classroom classroom = department.getClassrooms().stream()
                .filter(c -> c.getName().contains("10"))
                .findFirst()
                .orElseThrow(() -> ExceptionUtil.badRequest("classroom.10.notfound", locale));

        Section section = classroom.getSections().stream().findFirst()
                .orElseThrow(() -> ExceptionUtil.badRequest("section.notfound", locale));

        Student student = new Student();

        student.setStudentId(generateNextStudentId());
        student.setName(request.getName());
        student.setGender(request.getGender());
        student.setBirthplace(regency);
        student.setBirthDate(birthDate);
        student.setDistrictAddress(district);
        student.setAddress(request.getAddress());
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(birthDate.format(TimeFormat.formatter)));
        student.setEmail(request.getEmail());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setSection(section);
        student.setRole(Role.STUDENT);

        repository.save(student);
        System.out.println("ENNNDDDDDDDDDDD");
    }

    @Override
    @Transactional
    public void update(String id, StudentUpdate request, Locale locale) {
        validationService.validate(request);

        Student student = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.notFound("student.notfound", locale));

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

        Department department = departmentService.findByName(request.getDepartmentName())
                .orElseThrow(() -> ExceptionUtil.badRequest("department.notfound", locale));

        Classroom classroom = department.getClassrooms().stream()
                .filter(c -> c.getName().contains(request.getClassroomName()))
                .findFirst()
                .orElseThrow(() -> ExceptionUtil.badRequest("classroom.10.notfound", locale));

        Section section = classroom.getSections().stream()
                .filter(s -> s.getName().equals(request.getSectionName().charAt(0)))
                .findFirst()
                .orElseThrow(() -> ExceptionUtil.badRequest("section.notfound", locale));

        student.setName(request.getName());
        student.setGender(request.getGender());
        student.setBirthplace(regency);
        student.setBirthDate(birthDate);
        student.setDistrictAddress(district);
        student.setAddress(request.getAddress());
        student.setUsername(request.getUsername());
        student.setEmail(request.getEmail());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setSection(section);

        repository.save(student);
    }

    @Override
    public void delete(String id, Locale locale) {
        Student student = repository.findById(id)
                .orElseThrow(() -> ExceptionUtil.badRequest("student.notfound", locale));

        repository.delete(student);
    }

    @Transactional
    public StudentResponseTable generateStudentResponseTable(Student student) {
        return StudentResponseTable.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .gender(student.getGender())
                // .birthPlace(student.getBirthplace().getName())
                .birthDate(student.getBirthDate().format(TimeFormat.formatter2))
                .departmentName(student.getSection().getClassroom().getDepartment().getName())
                .classroomName(student.getSection().getClassroom().getName())
                .sectionName(String.valueOf(student.getSection().getName()))
                .build();
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

    public String generateNextStudentId() {
        String prefix = String.valueOf(Year.now().getValue());

        // Ambil ID terbesar di DB
        Optional<String> lastIdOpt = repository.findMaxStudentId();

        if (lastIdOpt.isEmpty()) {
            return prefix + "0001"; // kalau belum ada data sama sekali
        }

        String lastId = lastIdOpt.get();

        // Pisahkan tahun dari urutan
        String lastYearPart = lastId.substring(0, 4);
        String lastNumberPart = lastId.substring(4);

        int nextNumber;
        if (lastYearPart.equals(prefix)) {
            // masih di tahun yang sama → lanjut increment
            nextNumber = Integer.parseInt(lastNumberPart) + 1;
        } else {
            // tahun baru → mulai dari 1 lagi
            nextNumber = 1;
        }

        return String.format("%s%04d", prefix, nextNumber);
    }
}
