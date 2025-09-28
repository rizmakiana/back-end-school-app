package com.unindra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unindra.entity.Staff;
import com.unindra.model.request.RegisterStaffRequest;
import com.unindra.model.response.WebResponse;
import com.unindra.model.util.Gender;
import com.unindra.repository.StaffRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void testRegisterSuccessEn() throws JsonProcessingException, Exception {
        RegisterStaffRequest request = RegisterStaffRequest.builder()
                .name("Zahra")
                .gender(Gender.FEMALE)
                .birthPlaceRegency("3201")
                .birthDate("14")
                .birthMonth(4)
                .birthYear("2003")
                .districtAddress("320101")
                .address("Jatiasih No 17")
                .username("zahra")
                .password("rahasia")
                .confirmPassword("rahasia")
                .email("zahra@gmail.com")
                .phoneNumber("+6289278123")
                .build();

        mockMvc.perform(
                post("/api/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isOk()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertEquals("Staff registration completed successfully.", response.getMessage());
                });

        Staff staff = repository.findByUsername(request.getUsername()).orElse(null);
        assertNotNull(staff);
        assertEquals(request.getEmail(), staff.getEmail());
    }

    @Test
    public void testRegisterSuccessId() throws JsonProcessingException, Exception {
        RegisterStaffRequest request = RegisterStaffRequest.builder()
                .name("Zahra")
                .gender(Gender.FEMALE)
                .birthPlaceRegency("3201")
                .birthDate("14")
                .birthMonth(4)
                .birthYear("2003")
                .districtAddress("320101")
                .address("Jatiasih No 17")
                .username("zahra")
                .password("rahasia")
                .confirmPassword("rahasia")
                .email("zahra@gmail.com")
                .phoneNumber("+6289278123")
                .build();

        mockMvc.perform(
                post("/api/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "id")
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertEquals("Registrasi staf berhasil diselesaikan.", response.getMessage());
                    assertNull(response.getErrors());
                });

        Staff staff = repository.findByUsername(request.getUsername()).orElse(null);
        assertNotNull(staff);
        assertEquals(request.getEmail(), staff.getEmail());

    }

    @Test
    public void testRegisterFailPasswordnotEqualsEn() throws JsonProcessingException, Exception {
        RegisterStaffRequest request = RegisterStaffRequest.builder()
                .name("Zahra")
                .gender(Gender.FEMALE)
                .birthPlaceRegency("3201")
                .birthDate("14")
                .birthMonth(4)
                .birthYear("2003")
                .districtAddress("320101")
                .address("Jatiasih No 17")
                .username("zahra")
                .password("rahasia")
                .confirmPassword("rahasiabeda")
                .email("zahra@gmail.com")
                .phoneNumber("+6289278123")
                .build();

        mockMvc.perform(
                post("/api/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                            assertEquals("Password and confirm password do not match.", response.getErrors());
                            assertNotNull(response.getErrors());
                        });        
    }

    @Test
    public void testRegisterFailInvalidDatesEn() throws JsonProcessingException, Exception {
        RegisterStaffRequest request = RegisterStaffRequest.builder()
                .name("Zahra")
                .gender(Gender.FEMALE)
                .birthPlaceRegency("3201")
                .birthDate("30")
                .birthMonth(2)
                .birthYear("2003")
                .districtAddress("320101")
                .address("Jatiasih No 17")
                .username("zahra")
                .password("rahasia")
                .confirmPassword("rahasia")
                .email("zahra@gmail.com")
                .phoneNumber("+6289278123")
                .build();

        mockMvc.perform(
                post("/api/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                            assertEquals("The birth date is invalid. Please check the date, month, and year.", response.getErrors());
                            assertNotNull(response.getErrors());
                        });        
    }

    @Test
    public void testRegisterFailRegencyNotFoundEn() throws JsonProcessingException, Exception {
        RegisterStaffRequest request = RegisterStaffRequest.builder()
                .name("Zahra")
                .gender(Gender.FEMALE)
                .birthPlaceRegency("3200")
                .birthDate("14")
                .birthMonth(2)
                .birthYear("2003")
                .districtAddress("320101")
                .address("Jatiasih No 17")
                .username("zahra")
                .password("rahasia")
                .confirmPassword("rahasia")
                .email("zahra@gmail.com")
                .phoneNumber("+6289278123")
                .build();

        mockMvc.perform(
                post("/api/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                            assertNotNull(response.getErrors());
                            assertEquals("The specified birthplace regency could not be found.", response.getErrors());
                        });        
    }

    @Test
    public void testRegisterFailDistrictNotFoundEn() throws JsonProcessingException, Exception {
        RegisterStaffRequest request = RegisterStaffRequest.builder()
                .name("Zahra")
                .gender(Gender.FEMALE)
                .birthPlaceRegency("3201")
                .birthDate("14")
                .birthMonth(2)
                .birthYear("2003")
                .districtAddress("320100")
                .address("Jatiasih No 17")
                .username("zahra")
                .password("rahasia")
                .confirmPassword("rahasia")
                .email("zahra@gmail.com")
                .phoneNumber("+6289278123")
                .build();

        mockMvc.perform(
                post("/api/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                            assertNotNull(response.getErrors());
                            assertEquals("The specified district address could not be found.", response.getErrors());
                        });        
    }

    @Test
    public void testRegisterFailUsernameAlreadyExists() throws JsonProcessingException, Exception {
        Staff staff = new Staff();
        staff.setUsername("zahra");

        repository.save(staff);

        RegisterStaffRequest request = RegisterStaffRequest.builder()
                .name("Zahra")
                .gender(Gender.FEMALE)
                .birthPlaceRegency("3201")
                .birthDate("14")
                .birthMonth(2)
                .birthYear("2003")
                .districtAddress("320101")
                .address("Jatiasih No 17")
                .username("zahra")
                .password("rahasia")
                .confirmPassword("rahasia")
                .email("zahra@gmail.com")
                .phoneNumber("+6289278123")
                .build();

        mockMvc.perform(
                post("/api/staff")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                            assertNotNull(response.getErrors());
                            assertEquals("The username is already registered.", response.getErrors());
                        });        
    }
}