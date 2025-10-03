package com.unindra.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseHandled {

    private String courseName;

    private String sectionCode;
    
}
