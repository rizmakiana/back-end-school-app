package com.unindra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.request.CourseResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/staff/courses")
public class CourseController {
    
    private final CourseService service;

    @GetMapping
    public ResponseEntity<WebResponse<List<CourseResponse>>> getAll() {
        return ResponseEntity.ok(
            WebResponse.<List<CourseResponse>>builder()
                .data(service.getAll())
                .build()
        );
    }
}
