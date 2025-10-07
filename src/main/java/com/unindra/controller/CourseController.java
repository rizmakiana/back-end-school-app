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

import com.unindra.model.request.CourseRequest;
import com.unindra.model.request.CourseResponse;
import com.unindra.model.request.CourseUpdateRequest;
import com.unindra.model.response.WebResponse;
import com.unindra.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class CourseController {

    private final CourseService service;

    private final MessageSource source;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping(path = "/staff/courses")
    public ResponseEntity<WebResponse<List<CourseResponse>>> getAll() {
        return ResponseEntity.ok(
                WebResponse.<List<CourseResponse>>builder()
                        .data(service.getAll())
                        .build());
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(path = "/staff/courses")
    public ResponseEntity<WebResponse<String>> add(
            @RequestBody CourseRequest request,
            Locale locale) {
        service.add(request, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("course.created", null, locale))
                        .build());

    }

    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping(path = "/staff/courses/{id}")
    public ResponseEntity<WebResponse<String>> delete(
            @PathVariable String id,
            Locale locale) {
        service.delete(id, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("course.deleted", null, locale))
                        .build());
    }

    @PreAuthorize("hasRole('STAFF')")
    @PatchMapping(path = "/staff/courses/{id}")
    public ResponseEntity<WebResponse<String>> update(
        @PathVariable String id,
        @RequestBody CourseUpdateRequest request,
        Locale locale
    ) {
        service.update(id, request, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                    .message(source.getMessage("course.updated", null, locale))
                    .build()
        );
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping(path = "/staff/courses/{code}")
    public ResponseEntity<WebResponse<CourseResponse>> getByCode(@PathVariable String code, Locale locale) {
        return ResponseEntity.ok(
                WebResponse.<CourseResponse>builder()
                        .data(service.getByCode(code, locale))
                        .build());
    }
}
