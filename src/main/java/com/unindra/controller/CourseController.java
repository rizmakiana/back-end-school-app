package com.unindra.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.CourseRequest;
import com.unindra.model.request.CourseResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/staff/courses")
public class CourseController {

    private final CourseService service;

    private final MessageSource source;

    @GetMapping
    public ResponseEntity<WebResponse<List<CourseResponse>>> getAll(Authentication authentication) {
        return ResponseEntity.ok(
                WebResponse.<List<CourseResponse>>builder()
                        .data(service.getAll())
                        .build());
    }

    @PostMapping
    public ResponseEntity<WebResponse<String>> add(
            Authentication authentication,
            @RequestBody CourseRequest request,
            Locale locale) {
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("course.created", null, locale))
                        .build());

    }
}
