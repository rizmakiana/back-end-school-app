package com.unindra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.DepartmentRequest;
import com.unindra.model.response.DepartmentResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.DepartmentService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class DepartmentController {

	private final DepartmentService service;

	private final MessageSource source;

	@PreAuthorize("hasRole('STAFF')")
	@GetMapping(path = "/staff/departments")
	public ResponseEntity<WebResponse<List<DepartmentResponse>>> getAll(Authentication authentication) {

		WebResponse<List<DepartmentResponse>> response = WebResponse.<List<DepartmentResponse>>builder()
				.data(service.getAll())
				.build();

		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasRole('STAFF')")
	@PostMapping(path = "/staff/departments")
	public ResponseEntity<WebResponse<String>> create(
			@RequestBody DepartmentRequest request,
			Authentication authentication,
			Locale locale) {

		service.add(request, locale);
		return ResponseEntity.ok(WebResponse.<String>builder()
				.message(source.getMessage("department.created", null, locale)).build());
	}

	@PreAuthorize("hasRole('STAFF')")
	@PatchMapping(path = "/staff/departments/{id}")
	public ResponseEntity<WebResponse<String>> update(
			@PathVariable String id,
			@RequestBody DepartmentRequest request,
			Locale locale,
			Authentication authentication) {

		service.update(id, request, locale);
		return ResponseEntity.ok(WebResponse.<String>builder()
				.message(source.getMessage("department.updated", null, locale))
				.build());
	}

	@PreAuthorize("hasRole('STAFF')")
	@DeleteMapping(path = "staff/departments/{id}")
	public ResponseEntity<WebResponse<String>> delete(
			@PathVariable String id,
			Locale locale) {

		service.delete(id, locale);
		return ResponseEntity.ok(WebResponse.<String>builder()
				.message(source.getMessage("department.deleted", null, locale))
				.build());
	}

	@PreAuthorize("hasRole('STAFF')")
	@GetMapping(path = "/staff/departments/{code}")
	public ResponseEntity<WebResponse<DepartmentResponse>> getByCode(@PathVariable String code, Locale locale) {
		return ResponseEntity.ok(
				WebResponse.<DepartmentResponse>builder()
						.data(service.getByCode(code, locale))
						.build());
	}

}
