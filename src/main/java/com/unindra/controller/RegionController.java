package com.unindra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.response.ProvinceResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.ProvinceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/region")
public class RegionController {

    private final ProvinceService provinceService;
    
    @GetMapping("/provinces")
    public ResponseEntity<WebResponse<List<ProvinceResponse>>> getProvinces() {
        return ResponseEntity.ok(WebResponse.<List<ProvinceResponse>>builder()
                .data(provinceService.getProvinceList())
                .build());
    }
    
}
