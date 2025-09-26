package com.unindra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.unindra.model.response.RegencyResponse;
import com.unindra.model.response.RegencyResponse.RegencyResponseBuilder;
import com.unindra.repository.RegencyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegencyService {

    private final RegencyRepository regencyRepository;

    public List<RegencyResponse> getRegencyListByProvinceId(String provinceId) {
        return regencyRepository.findByProvinceId(provinceId).stream()
                .map(regency -> RegencyResponse.builder()
                        .id(regency.getId())
                        .name(regency.getName())
                        .build())
                .toList();
    }
}
