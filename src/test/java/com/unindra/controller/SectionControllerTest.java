package com.unindra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.entity.Section;
import com.unindra.entity.Staff;
import com.unindra.model.request.SectionRequest;
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
            char name = (char) ('A' + i);
            section.setName(name);
            section.setCode(String.format("%s %c", c.getCode(), name));

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
    public void getAllSuccess() throws Exception {

        Staff staff = staffRepository.findByUsername("zahra").orElse(null);
        TokenResponse token = jwtUtil.generateToken(staff);

        mockMvc.perform(
                get("/api/sections")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<SectionResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<SectionResponse>>>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData());
                    assertEquals(response.getData().size(), 10);

                });
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

    @Test
    public void createdSuccess() throws Exception {

        Staff staff = staffRepository.findByUsername("zahra").orElse(null);
        TokenResponse token = jwtUtil.generateToken(staff);

        SectionRequest request = SectionRequest.builder()
                .departmentName("Materi Ilmu Pengetahuan Alam")
                .classroomName("10")
                .build();

        mockMvc.perform(
                post("/api/sections")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<String>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<String>>>() {
                            });

                    assertNull(response.getErrors());

                });
    }

    @Test
    public void createdFailUnathorized() throws Exception {

        String token = UUID.randomUUID().toString();

        SectionRequest request = SectionRequest.builder()
                .departmentName("Materi Ilmu Pengetahuan Alam")
                .classroomName("10")
                .build();

        mockMvc.perform(
                post("/api/sections")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    
                    assertNotNull(responseBody);
                });
    }

    @Test
    public void testDeleteSucces() throws JsonProcessingException, Exception {
        
        Staff staff = staffRepository.findByUsername("zahra").orElse(null);
        TokenResponse token = jwtUtil.generateToken(staff);

        Section section = repository.findByCode("MIPA10 A").orElse(null);
        mockMvc.perform(
                delete("/api/sections/" + section.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<String>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<String>>>() {
                            });

                    assertNull(response.getErrors());

                });
        assertFalse(repository.existsByCode("MIPA10 A"));

    }
}
