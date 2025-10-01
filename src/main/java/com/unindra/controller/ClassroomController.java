package com.unindra.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.response.ClassroomResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.ClassroomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/classrooms")
public class ClassroomController {

    private final ClassroomService service;

    private final MessageSource source;

    @GetMapping
    public ResponseEntity<WebResponse<List<ClassroomResponse>>> getAll(Authentication authentication, Locale locale) {
        return ResponseEntity.ok(
                WebResponse.<List<ClassroomResponse>>builder()
                        .data(service.getAll())
                        .message(source.getMessage("classroom.created", null, locale))
                        .build());

    }
}
