package com.unindra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.unindra.entity.Staff;
import com.unindra.model.request.ClassroomRequest;
import com.unindra.model.response.ClassroomResponse;
import com.unindra.model.response.TokenResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.repository.ClassroomRepository;
import com.unindra.repository.DepartmentRepository;
import com.unindra.repository.StaffRepository;
import com.unindra.security.BCrypt;
import com.unindra.util.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClassroomControllerTest {

    @Autowired
    private ClassroomRepository repository;

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
        departmentRepository.deleteAll();
        staffRepository.deleteAll();

        Department d = new Department();
        d.setName("Materi Ilmu Pengetahuan Alam");
        d.setCode("MIPA");
        departmentRepository.save(d);

        for (int i = 10; i < 13; i++) {
            Classroom c = new Classroom();

            c.setDepartment(d);
            c.setName(String.valueOf(i));
            c.setCode(d.getCode() + c.getName());

            repository.save(c);
        }

        staffRepository.deleteAll();

        Staff staff = new Staff();

        staff.setName("Zahra Hanifa");
        staff.setUsername("zahra");
        staff.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));

        staffRepository.save(staff);
    }

    @Test
    public void getAllSuccess() throws Exception {
        Staff staff = staffRepository.findByUsername("zahra").orElse(null);

        TokenResponse token = jwtUtil.generateToken(staff);

        mockMvc.perform(
                get("/api/classrooms")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<ClassroomResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<ClassroomResponse>>>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData());
                    assertEquals(response.getData().size(), 3);
                });

        assertTrue(repository.existsByCode("MIPA10"));
        assertTrue(repository.existsByCode("MIPA11"));
        assertTrue(repository.existsByCode("MIPA12"));
    }

    @Test
    public void createdSuccess() throws Exception {
        repository.deleteAll();

        Staff staff = staffRepository.findByUsername("zahra").orElse(null);

        TokenResponse token = jwtUtil.generateToken(staff);
        ClassroomRequest classroomRequest = ClassroomRequest.builder()
                .departmentName("Materi Ilmu Pengetahuan Alam")
                .classroomName("10")
                .code("MIPA10")
                .build();

        mockMvc.perform(
                post("/api/classrooms")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(classroomRequest)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNull(response.getErrors());
                });

        assertTrue(repository.existsByCode("MIPA10"));
    }

    @Test
    public void createdFailValidation() throws Exception {
        repository.deleteAll();

        Staff staff = staffRepository.findByUsername("zahra").orElse(null);

        TokenResponse token = jwtUtil.generateToken(staff);
        ClassroomRequest classroomRequest = ClassroomRequest.builder()
                .departmentName("")
                .classroomName("")
                .code("")
                .build();

        mockMvc.perform(
                post("/api/classrooms")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(classroomRequest)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNotNull(response.getErrors());
                });

        assertFalse(repository.existsByCode("MIPA10"));
    }

    @Test
    public void createdFailUnauth() throws Exception {
        repository.deleteAll();

        String token = UUID.randomUUID().toString();
        ClassroomRequest classroomRequest = ClassroomRequest.builder()
                .departmentName("Materi Ilmu Pengetahuan Alam")
                .classroomName("10")
                .code("MIPA10")
                .build();

        mockMvc.perform(
                post("/api/classrooms")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(classroomRequest)))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertNotNull(responseBody);
                });
    }

    @Test
    public void deleteSucces() throws Exception {
        
        Staff staff = staffRepository.findByUsername("zahra").orElse(null);

        TokenResponse token = jwtUtil.generateToken(staff);

        Department dept = departmentRepository.findByName("Materi Ilmu Pengetahuan alam").orElse(null);
        Classroom classroom = repository.findByDepartmentAndName(dept, "10").orElse(null);
        mockMvc.perform(
                delete("/api/classrooms/" + classroom.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNull(response.getErrors());
                });
        assertFalse(repository.existsByDepartmentAndName(dept, "10"));
    }
}
 