package com.unindra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.model.response.DistrictResponse;
import com.unindra.model.response.ProvinceResponse;
import com.unindra.model.response.RegencyResponse;
import com.unindra.model.response.WebResponse;
import com.unindra.service.DistrictService;
import com.unindra.service.ProvinceService;
import com.unindra.service.RegencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/region")
public class RegionController {

    private final ProvinceService provinceService;

    private final RegencyService regencyService;

    private final DistrictService districtService;

    @GetMapping("/provinces")
    public ResponseEntity<WebResponse<List<ProvinceResponse>>> getProvinces() {
        return ResponseEntity.ok(WebResponse.<List<ProvinceResponse>>builder()
                .data(provinceService.getProvinceList())
                .build());
    }

    @GetMapping("/provinces/{provinceId}/regencies")
    public ResponseEntity<WebResponse<List<RegencyResponse>>> getRegencyListByProvinceId(
            @PathVariable String provinceId) {
        return ResponseEntity.ok(WebResponse.<List<RegencyResponse>>builder()
                .data(regencyService.getRegencyListByProvinceId(provinceId))
                .build());
    }

    @GetMapping("/provinces/{provinceId}/regencies/{regencyId}/districts")
    public ResponseEntity<WebResponse<List<DistrictResponse>>> getDistrictListByProvinceId(
            @PathVariable String provinceId, @PathVariable String regencyId) {
        return ResponseEntity.ok(WebResponse.<List<DistrictResponse>>builder()
                .data(districtService.getDistrictListByRegencyId(regencyId))
                .build());
    }

}
