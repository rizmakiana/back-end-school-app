package com.unindra.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.ClassroomRequest;
import com.unindra.model.response.ClassroomResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.ClassroomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class ClassroomController {

	private final ClassroomService service;

	private final MessageSource source;

	@PreAuthorize("hasRole('STAFF')")
	@GetMapping(path = "/staff/classrooms")
	public ResponseEntity<WebResponse<List<ClassroomResponse>>> getAll(Locale locale) {
		return ResponseEntity.ok(
				WebResponse.<List<ClassroomResponse>>builder()
						.data(service.getAll())
						.build());

	}

	@PreAuthorize("hasRole('STAFF')")
	@PostMapping(path = "/staff/classrooms")
	public ResponseEntity<WebResponse<String>> create(
			@RequestBody ClassroomRequest request,
			Locale locale) {

		service.add(request, locale);
		return ResponseEntity.ok(
				WebResponse.<String>builder()
						.message(source.getMessage("classroom.created", null, locale))
						.build());
	}

	@PreAuthorize("hasRole('STAFF')")
	@DeleteMapping(path = "/staff/classrooms/{id}")
	public ResponseEntity<WebResponse<String>> delete(
			@PathVariable String id,
			Locale locale) {

		service.delete(id, locale);
		return ResponseEntity.ok(
				WebResponse.<String>builder()
						.message(source.getMessage("classroom.deleted", null, locale))
						.build());

	}
}
