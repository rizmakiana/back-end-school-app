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

import com.unindra.model.request.ClassroomRequest;
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
                        .build());

    }

    @PostMapping
    public ResponseEntity<WebResponse<String>> create(
            @RequestBody ClassroomRequest request,
            Authentication authentication,
            Locale locale) {

        service.add(request, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("classroom.created", null, locale))
                        .build());
    }

    @DeleteMapping(path = "/{id}")
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
