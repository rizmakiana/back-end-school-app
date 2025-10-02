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

import com.unindra.model.request.SectionRequest;
import com.unindra.model.response.SectionResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.SectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/sections")
public class SectionController {

    private final SectionService service;

    private final MessageSource source;

    @GetMapping
    public ResponseEntity<WebResponse<List<SectionResponse>>> getAll(Authentication authentication, Locale locale) {
        return ResponseEntity.ok(
                WebResponse.<List<SectionResponse>>builder().data(service.getAll())
                        .build());
    }

    @PostMapping
    public ResponseEntity<WebResponse<String>> add(
            Authentication authentication,
            @RequestBody SectionRequest request,
            Locale locale) {

        service.add(request, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("section.created", null, locale))
                        .build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<WebResponse<String>> delete(
            Authentication authentication,
            @PathVariable String id,
            Locale locale) {
        service.delete(id, locale);
        return ResponseEntity.ok(
                WebResponse.<String>builder()
                        .message(source.getMessage("section.deleted", null, locale)).build());
    }

}
