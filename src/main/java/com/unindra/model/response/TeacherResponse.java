package com.unindra.model.response;

import java.time.LocalDate;
import java.util.List;

import com.unindra.model.util.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherResponse {
    
    private String id;

    private String name;

    private Gender gender;

    private String birthPlace;

    private LocalDate birthDate;

    private String districtAddress;

    private String address;

    private String username;

    private String email;

    private String phoneNumber;

    private List<CourseHandled> courseHandleds;
}
