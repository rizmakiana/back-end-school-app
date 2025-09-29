package com.unindra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import com.unindra.entity.District;
import com.unindra.entity.Regency;
import com.unindra.entity.Staff;
import com.unindra.model.request.LoginRequest;
import com.unindra.model.response.TokenResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.model.util.Gender;
import com.unindra.repository.DistrictRepository;
import com.unindra.repository.RegencyRepository;
import com.unindra.repository.StaffRepository;
import com.unindra.security.BCrypt;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RegencyRepository regencyRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @BeforeEach
    public void before() {
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
    void loginStaffSuccessWithUsername() throws JsonProcessingException, Exception {

        LoginRequest request = LoginRequest.builder()
                .username("zahra")
                .password("rahasia")
                .build();

        mockMvc.perform(
                post("/api/auth/login/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getMessage());
					assertNotNull(response.getData());
                    assertEquals("Login successfully.", response.getMessage());

                    assertNotNull(response.getData());
                });
    }

    @Test
    void loginStaffSuccessWithPhoneNumber() throws JsonProcessingException, Exception {

        LoginRequest request = LoginRequest.builder()
                .username("0831341341")
                .password("rahasia")
                .build();

        mockMvc.perform(
                post("/api/auth/login/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getMessage());
                    assertEquals("Login successfully.", response.getMessage());

                    assertNotNull(response.getData());
                });
    }

    @Test
    void loginStaffSuccessWithEmail() throws JsonProcessingException, Exception {

        LoginRequest request = LoginRequest.builder()
                .username("zahra@gmail.com")
                .password("rahasia")
                .build();

        mockMvc.perform(
                post("/api/auth/login/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getMessage());
                    assertEquals("Login successfully.", response.getMessage());

                    assertNotNull(response.getData());
                });
    }

    @Test
    void loginStaffFailUsernameNotFound() throws JsonProcessingException, Exception {

        LoginRequest request = LoginRequest.builder()
                .username("zahrabeda@gmail.com")
                .password("rahasia")
                .build();

        mockMvc.perform(
                post("/api/auth/login/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                            });

                    assertNotNull(response.getErrors());
                    assertEquals("Username or Password wrong.", response.getErrors());
                });
    }

    @Test
    void loginStaffFailPasswordWrongFound() throws JsonProcessingException, Exception {

        LoginRequest request = LoginRequest.builder()
                .username("zahra@gmail.com")
                .password("rahasiabeda")
                .build();

        mockMvc.perform(
                post("/api/auth/login/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                            });

                    assertNotNull(response.getErrors());
                    assertEquals("Username or Password wrong.", response.getErrors());
                });
    }

    @Test
    void loginStaffFailValidationUsernameBlank() throws JsonProcessingException, Exception {

        LoginRequest request = LoginRequest.builder()
                .username("")
                .password("rahasiabeda")
                .build();

        mockMvc.perform(
                post("/api/auth/login/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void loginStaffFailValidationPasswordBlank() throws JsonProcessingException, Exception {

        LoginRequest request = LoginRequest.builder()
                .username("")
                .password("rahasiabeda")
                .build();

        mockMvc.perform(
                post("/api/auth/login/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }
}
