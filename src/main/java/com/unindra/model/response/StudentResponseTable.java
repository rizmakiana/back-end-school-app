package com.unindra.model.response;

import com.unindra.model.util.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponseTable {
    
    private String studentId;

    private String name;

    private Gender gender;

    private String birthPlace;

    private String birthDate;

    private String departmentName;

    private String classroomName;

    private String sectionName;

}