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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unindra.entity.Classroom;
import com.unindra.entity.Department;
import com.unindra.entity.Staff;
import com.unindra.model.request.ClassroomRequest;
import com.unindra.model.request.LoginRequest;
import com.unindra.model.response.ClassroomResponse;
import com.unindra.model.response.TokenResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.model.util.Role;
import com.unindra.repository.ClassroomRepository;
import com.unindra.repository.DepartmentRepository;
import com.unindra.repository.StaffRepository;

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
	private StaffRepository staffRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() throws Exception {
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
		staff.setPassword(passwordEncoder.encode("rahasia"));
		staff.setRole(Role.STAFF);

		staffRepository.save(staff);
	}

	@Test
	public void getAllSuccess() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder()
				.username("zahra")
				.password("rahasia")
				.build();

		MvcResult loginResult = mockMvc.perform(
				post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		WebResponse<TokenResponse> loginResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				new TypeReference<WebResponse<TokenResponse>>() {
				});

		String token = loginResponse.getData().getToken();

		mockMvc.perform(
				get("/api/staff/classrooms")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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

		LoginRequest loginRequest = LoginRequest.builder()
				.username("zahra")
				.password("rahasia")
				.build();

		MvcResult loginResult = mockMvc.perform(
				post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		WebResponse<TokenResponse> loginResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				new TypeReference<WebResponse<TokenResponse>>() {
				});

		String token = loginResponse.getData().getToken();

		ClassroomRequest classroomRequest = ClassroomRequest.builder()
				.departmentName("Materi Ilmu Pengetahuan Alam")
				.classroomName("10")
				.code("MIPA10")
				.build();

		mockMvc.perform(
				post("/api/staff/classrooms")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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

		LoginRequest loginRequest = LoginRequest.builder()
				.username("zahra")
				.password("rahasia")
				.build();

		MvcResult loginResult = mockMvc.perform(
				post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		WebResponse<TokenResponse> loginResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				new TypeReference<WebResponse<TokenResponse>>() {
				});

		String token = loginResponse.getData().getToken();

		ClassroomRequest classroomRequest = ClassroomRequest.builder()
				.departmentName("")
				.classroomName("")
				.code("")
				.build();

		mockMvc.perform(
				post("/api/staff/classrooms")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
				post("/api/staff/classrooms")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.content(objectMapper.writeValueAsString(classroomRequest)))
				.andExpectAll(
						status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void deleteSucces() throws Exception {

		LoginRequest loginRequest = LoginRequest.builder()
				.username("zahra")
				.password("rahasia")
				.build();

		MvcResult loginResult = mockMvc.perform(
				post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		WebResponse<TokenResponse> loginResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				new TypeReference<WebResponse<TokenResponse>>() {
				});

		String token = loginResponse.getData().getToken();

		Department dept = departmentRepository.findByName("Materi Ilmu Pengetahuan alam").orElse(null);
		Classroom classroom = repository.findByDepartmentAndName(dept, "10").orElse(null);
		mockMvc.perform(
				delete("/api/staff/classrooms/" + classroom.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
