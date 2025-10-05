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

import com.unindra.model.request.TeacherRequest;
import com.unindra.model.request.TeacherUpdate;
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

    @PreAuthorize("hasRole('STAFF')")
    @PatchMapping(path = "/staff/teachers/{id}")
    public void update(@PathVariable String id, TeacherUpdate request, Locale locale) {
        service.update(id, request, locale);
        ResponseEntity.ok(
            WebResponse.<String>builder()
            .message(source.getMessage("teacher.updated", null, locale))
            .build()
        );
    }

    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping(path = "/staff/teachers/{id}")
    public void delete(@PathVariable String id, Locale locale) {
        service.delete(id, locale);
        ResponseEntity.ok(
            WebResponse.<String>builder()
            .message(source.getMessage("teacher.updated", null, locale))
            .build()
        );
    }


}
