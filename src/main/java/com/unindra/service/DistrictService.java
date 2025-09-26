package com.unindra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.unindra.model.response.DistrictResponse;
import com.unindra.repository.DistrictRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;

    public List<DistrictResponse> getDistrictListByRegencyId(String regencyId) {
        return districtRepository.findByRegencyId(regencyId).stream()
                .map(district -> DistrictResponse.builder()
                        .id(district.getId())
                        .name(district.getName())
                        .build())
                .toList();
    }
}
