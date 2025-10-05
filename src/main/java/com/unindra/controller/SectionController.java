package com.unindra.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.SectionRequest;
import com.unindra.model.request.SectionUpdateRequest;
import com.unindra.model.response.SectionResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.SectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class SectionController {

	private final SectionService service;

	private final MessageSource source;

	@PreAuthorize("hasRole('STAFF')")
	@GetMapping(path = "/staff/sections")
	public ResponseEntity<WebResponse<List<SectionResponse>>> getAll(Locale locale) {
		return ResponseEntity.ok(
				WebResponse.<List<SectionResponse>>builder().data(service.getAll())
						.build());
	}

	@PreAuthorize("hasRole('STAFF')")
	@PostMapping(path = "/staff/sections")
	public ResponseEntity<WebResponse<String>> add(
			@RequestBody SectionRequest request,
			Locale locale) {

		service.add(request, locale);
		return ResponseEntity.ok(
				WebResponse.<String>builder()
						.message(source.getMessage("section.created", null, locale))
						.build());
	}

	@PreAuthorize("hasRole('STAFF')")
	@DeleteMapping(path = "staff/sections/{id}")
	public ResponseEntity<WebResponse<String>> delete(
			@PathVariable String id,
			Locale locale) {
		service.delete(id, locale);
		return ResponseEntity.ok(
				WebResponse.<String>builder()
						.message(source.getMessage("section.deleted", null, locale)).build());
	}

	@PreAuthorize("hasRole('STAFF')")
	@PatchMapping(path = "/staff/sections/{id}")
	public ResponseEntity<WebResponse<String>> update(
			@PathVariable String id,
			@RequestBody SectionUpdateRequest request,
			Locale locale) {

		service.update(id, request, locale);
		return ResponseEntity.ok(
				WebResponse.<String>builder()
						.message(source.getMessage("section.updated.succesfully", null, locale))
						.build());

	}

}