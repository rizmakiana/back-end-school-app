package com.unindra.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.TeacherRequest;
import com.unindra.model.response.TeacherResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class TeacherController {

    private final TeacherService service;

    private final MessageSource source;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping(path = "/staff/teachers")
    public void getAll() {
        ResponseEntity.ok(
                WebResponse.<List<TeacherResponse>>builder()
                        .data(service.getAll())
                        .build());
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(path = "/staff/teachers")
    public void add(@RequestBody TeacherRequest request, Locale locale) {
        service.add(request, locale);
        ResponseEntity.ok(
            WebResponse.<String>builder()
            .message(source.getMessage("teacher.created", null, locale))
            .build()
        );
    }
}
