package com.unindra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.response.TeacherResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class TeacherController {
    
    private final TeacherService service;

    @GetMapping(path = "/staff/teachers")
    public void getAll() {
        ResponseEntity.ok(
            WebResponse.<List<TeacherResponse>>builder()
            .data(service.getAll())
            .build()
        );
    }
}
