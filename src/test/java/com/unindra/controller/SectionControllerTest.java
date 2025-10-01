package com.unindra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.entity.Section;
import com.unindra.entity.Staff;
import com.unindra.model.response.SectionResponse;
import com.unindra.model.response.TokenResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.repository.ClassroomRepository;
import com.unindra.repository.DepartmentRepository;
import com.unindra.repository.SectionRepository;
import com.unindra.repository.StaffRepository;
import com.unindra.security.BCrypt;
import com.unindra.util.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SectionControllerTest {
    
    @Autowired
    private SectionRepository repository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StaffRepository staffRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        classroomRepository.deleteAll();
        departmentRepository.deleteAll();
        staffRepository.deleteAll();

        Department d = new Department();
        d.setName("Materi Ilmu Pengetahuan Alam");
        d.setCode("MIPA");
        departmentRepository.save(d);

        Classroom c = new Classroom();
        c.setDepartment(d);
        c.setName("10");
        c.setCode(d.getCode() + c.getName());
        classroomRepository.save(c);

        for (int i = 0; i < 10; i++) {
            Section section = new Section();

            section.setClassroom(c);
            section.setName((char) i);
            section.setCode(String.format("%s %s", c.getCode(), (char)i));

            repository.save(section);
        }
        

        staffRepository.deleteAll();

        Staff staff = new Staff();

        staff.setName("Zahra Hanifa");
        staff.setUsername("zahra");
        staff.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));

        staffRepository.save(staff);
    }

    @Test
    public void getAllFailValidation() throws Exception {
        
        String token = UUID.randomUUID().toString();

        mockMvc.perform(
                get("/api/sections")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();

                    assertNotNull(response);
                });
    }
}
