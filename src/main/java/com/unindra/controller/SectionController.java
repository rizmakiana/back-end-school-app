package com.unindra.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.response.SectionResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.SectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/sections")
public class SectionController {

    private final SectionService service;

    @GetMapping
    public ResponseEntity<WebResponse<List<SectionResponse>>> getAll(Authentication authentication, Locale locale) {
        return ResponseEntity.ok(
                WebResponse.<List<SectionResponse>>builder().data(service.getAll())
                        .build());
    }

}
