package com.unindra.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import com.unindra.entity.Department;
import com.unindra.entity.District;
import com.unindra.entity.Regency;
import com.unindra.entity.Staff;
import com.unindra.model.response.DepartmentResponse;
import com.unindra.model.response.TokenResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.model.util.Gender;
import com.unindra.repository.DepartmentRepository;
import com.unindra.repository.DistrictRepository;
import com.unindra.repository.RegencyRepository;
import com.unindra.repository.StaffRepository;
import com.unindra.security.BCrypt;
import com.unindra.util.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DepartmentControllerTest {

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RegencyRepository regencyRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        staffRepository.deleteAll();

        Department d = new Department();
        d.setName("Materi Ilmu Pengetahuan Alam");
        d.setCode("MIPA");
        repository.save(d);

        Department f = new Department();
        f.setName("Ilmu Pengetahuan Sosial");
        f.setCode("IPS");
        repository.save(f);

        staffRepository.deleteAll();

        Staff staff = new Staff();

        Regency regency = regencyRepository.findById("3201").orElse(null);
        District district = districtRepository.findById("320101").orElse(null);

        staff.setName("Zahra Hanifa");
        staff.setGender(Gender.FEMALE);
        staff.setBirthDate(LocalDate.of(2003, 4, 14));
        staff.setBirthplace(regency);
        staff.setDistrictAddress(district);
        staff.setAddress("Kp. Jatiasih no 96");
        staff.setUsername("zahra");
        staff.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        staff.setEmail("zahra@gmail.com");
        staff.setPhoneNumber("0831341341");

        staffRepository.save(staff);

    }

    @Test
    public void testGetAll_Success() throws Exception {
        Staff staff = staffRepository.findByUsername("zahra").orElse(null);

        TokenResponse token = jwtUtil.generateToken(staff);

        mockMvc.perform(
                get("/api/staff/departments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<DepartmentResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<DepartmentResponse>>>() {
                            });

                    assertNotNull(response);
                    assertNotNull(response.getData());
                    assertFalse(response.getData().isEmpty());
                });
    }

    @Test
    public void testGetAll_Unauthorized() throws Exception {
        // Staff staff = staffRepository.findByUsername("zahra").orElse(null);

        // ini token valid (ga dipakai di test ini, cuma buat pembanding aja)
        // TokenResponse token = jwtUtil.generateToken(staff);

        // ini token random (invalid)
        String invalidToken = UUID.randomUUID().toString();

        mockMvc.perform(
                get("/api/staff/departments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertNotNull(responseBody);
                    System.out.println("Unauthorized response: " + responseBody);
                });
    }

}
