package com.unindra.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.StudentRequest;
import com.unindra.model.response.StudentResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class StudentController {

    private final StudentService service;

    private final MessageSource source;

    @GetMapping(path = "/staff/students")
    public ResponseEntity<WebResponse<List<StudentResponse>>> getAll(Authentication authentication) {
        return ResponseEntity.ok(
                WebResponse.<List<StudentResponse>>builder()
                        .data(service.getAll())
                        .build());
    }

    @DeleteMapping(path = "/api/staff/students/{id}")
    public ResponseEntity<WebResponse<String>> delete(
            Authentication authentication,
            @PathVariable String id,
            Locale locale) {
        service.delete(id, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("student.deleted", null, locale))
                        .build());
    }

    @PostMapping(path = "/staff/students")
    public ResponseEntity<WebResponse<String>> add(
            Authentication authentication,
            @RequestBody StudentRequest request,
            Locale locale) {
        service.add(request, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("student.registered", null, locale))
                        .build());
    }
}
