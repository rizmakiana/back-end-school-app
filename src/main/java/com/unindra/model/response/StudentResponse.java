package com.unindra.model.response;

import java.time.LocalDate;

import com.unindra.model.util.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponse {
    
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

    private String departmentName;

    private String classroomName;

    private String sectionName;

}