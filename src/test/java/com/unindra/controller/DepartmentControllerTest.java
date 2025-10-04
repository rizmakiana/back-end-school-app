package com.unindra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import com.unindra.entity.Department;
import com.unindra.entity.District;
import com.unindra.entity.Regency;
import com.unindra.entity.Staff;
import com.unindra.model.request.DepartmentRequest;
import com.unindra.model.request.LoginRequest;
import com.unindra.model.response.DepartmentResponse;
import com.unindra.model.response.TokenResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.model.util.Gender;
import com.unindra.model.util.Role;
import com.unindra.repository.ClassroomRepository;
import com.unindra.repository.DepartmentRepository;
import com.unindra.repository.DistrictRepository;
import com.unindra.repository.RegencyRepository;
import com.unindra.repository.SectionRepository;
import com.unindra.repository.StaffRepository;
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
	private StaffRepository staffRepository;

	@Autowired
	private ClassroomRepository classroomRepository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void setup() {
		sectionRepository.deleteAll();
		classroomRepository.deleteAll();
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

		staff.setName("Zahra Hanifa");
		staff.setGender(Gender.FEMALE);
		staff.setUsername("zahra");
		staff.setPassword(passwordEncoder.encode("rahasia"));
		staff.setEmail("zahra@gmail.com");
		staff.setPhoneNumber("0831341341");
		staff.setRole(Role.STAFF);

		staffRepository.save(staff);

	}

	@Test
	public void testGetAll_Success() throws Exception {

		// Log in terlebih dahulu
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

		// Ambil token dari response
		WebResponse<TokenResponse> loginResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				new TypeReference<WebResponse<TokenResponse>>() {
				});

		String token = loginResponse.getData().getToken();

		// Test hit endpoint
		mockMvc.perform(
				get("/api/staff/departments")
						.header("Authorization", "Bearer " + token)
						.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isOk())
				.andDo(result -> {
					WebResponse<List<DepartmentResponse>> response = objectMapper
							.readValue(
									result.getResponse()
											.getContentAsString(),
									new TypeReference<WebResponse<List<DepartmentResponse>>>() {
									});

					assertNotNull(response);
					assertNotNull(response.getData());
					assertFalse(response.getData().isEmpty());
				});
	}

	@Test
	public void testGetAll_Unauthorized() throws Exception {
		String invalidToken = UUID.randomUUID().toString();

		mockMvc.perform(
				get("/api/staff/departments")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void testGetAllFail_NotEqualsRole() throws Exception {

		Staff staff = new Staff();

		staff.setName("Zahra Hanifa M");
		staff.setGender(Gender.FEMALE);
		staff.setUsername("zahra2");
		staff.setPassword(passwordEncoder.encode("rahasia2"));
		staff.setEmail("zahra@gmail.com");
		staff.setPhoneNumber("0831341341");
		staff.setRole(Role.TEACHER);

		staffRepository.save(staff);

		LoginRequest loginRequest = LoginRequest.builder()
				.username("zahra2")
				.password("rahasia2")
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
				get("/api/staff/departments")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isForbidden())
				.andReturn();
	}

	@Test
	public void testCreatedDepartmentSuccess() throws Exception {
		
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

		DepartmentRequest request = DepartmentRequest.builder()
				.name("Akuntansi")
				.code("AK")
				.build();

		mockMvc.perform(
				post("/api/staff/departments")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertNotNull(response);
					assertNotNull(response.getMessage());
					assertEquals("Department created succesfully", response.getMessage());
				});
		assertTrue(repository.existsByName(request.getName()));
		assertTrue(repository.existsByCode(request.getCode()));
	}

	@Test
	public void testCreatedDepartmentFailIsExists() throws Exception {
		
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

		DepartmentRequest request = DepartmentRequest.builder()
				.name("Materi Ilmu Pengetahuan Alam")
				.code("MIPA")
				.build();

		mockMvc.perform(
				post("/api/staff/departments")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertNull(response.getMessage());
					assertNotNull(response.getErrors());
					assertEquals("Department already exists", response.getErrors());
				});
		assertTrue(repository.existsByName(request.getName()));
		assertTrue(repository.existsByCode(request.getCode()));
	}

	@Test
	public void testCreatedDepartmentFailBlankField() throws Exception {
		
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

		DepartmentRequest request = DepartmentRequest.builder()
				.name("")
				.code("")
				.build();

		mockMvc.perform(
				post("/api/staff/departments")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertEquals("Validation failed", response.getMessage());
					assertNotNull(response.getErrors());
				});
	}

	@Test
	public void testUpdatedDepartmentSuccess() throws Exception {
		
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
		
		Department dept = repository.findByCode("MIPA").orElse(null);
		DepartmentRequest request = DepartmentRequest.builder()
				.name("Ilmu Pengetahuan Alam")
				.code("IPA")
				.build();

		mockMvc.perform(
				patch("/api/staff/departments/" + dept.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertNull(response.getErrors());
				});
		Department dept2 = repository.findByCode("IPA").orElse(null);

		assertEquals(dept.getId(), dept2.getId());
		assertEquals(request.getName(), dept2.getName());
		assertEquals(request.getCode(), dept2.getCode());
	}

	@Test
	public void testUpdatedDepartmentFailExists() throws Exception {
		// Log in terlebih dahulu
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

		// Ambil token dari response
		WebResponse<TokenResponse> loginResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				new TypeReference<WebResponse<TokenResponse>>() {
				});

		String token = loginResponse.getData().getToken();
		
		Department dept = repository.findByCode("MIPA").orElse(null);
		DepartmentRequest request = DepartmentRequest.builder()
				.name("Materi Ilmu Pengetahuan Alam")
				.code("IPS")
				.build();

		mockMvc.perform(
				patch("/api/staff/departments/" + dept.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertNotNull(response.getErrors());
				});
		Department dept2 = repository.findByCode("MIPA").orElse(null);

		assertNotNull(dept2);
		assertEquals(dept.getId(), dept2.getId());
		assertEquals(dept.getName(), dept2.getName());
		assertEquals(dept.getCode(), dept2.getCode());
	}

	@Test
	public void testUpdatedDepartmentFailValidation() throws Exception {
		
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
		Department dept = repository.findByCode("MIPA").orElse(null);

		DepartmentRequest request = DepartmentRequest.builder()
				.name("")
				.code("")
				.build();

		mockMvc.perform(
				patch("/api/staff/departments/" + dept.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertEquals("Validation failed", response.getMessage());
					assertNotNull(response.getErrors());
				});
	}

	@Test
	public void testDeletedDepartmentSuccess() throws Exception {
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
		
		Department dept = repository.findByCode("MIPA").orElse(null);
		mockMvc.perform(
				delete("/api/staff/departments/" + dept.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertEquals("Department deleted succesfully", response.getMessage());
					assertNull(response.getErrors());
				});
		Department department = repository.findById(dept.getId()).orElse(null);

		assertNull(department);
		assertFalse(repository.existsByName(dept.getName()));
		assertFalse(repository.existsByName(dept.getCode()));
	}

	@Test
	@Disabled
	public void testDeletedDepartmentFail() throws Exception {
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
		
		Department dept = repository.findByCode("MIPA").orElse(null);
		mockMvc.perform(
				delete("/api/staff/departments/" + dept.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk())
				.andDo(result -> {
					WebResponse<String> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<WebResponse<String>>() {
							});

					assertEquals("Department deleted succesfully", response.getMessage());
					assertNull(response.getErrors());
				});
		Department department = repository.findById(dept.getId()).orElse(null);

		assertNull(department);
		assertFalse(repository.existsByName(dept.getName()));
		assertFalse(repository.existsByName(dept.getCode()));
	}
}
