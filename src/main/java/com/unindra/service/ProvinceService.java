package com.unindra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.unindra.model.response.ProvinceResponse;
import com.unindra.repository.ProvinceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository repository;

    public List<ProvinceResponse> getProvinceList() {
        return repository.findAll().stream()
                .map(province -> ProvinceResponse.builder()
                        .id(province.getId())
                        .name(province.getName()).build())
                .toList();
    }
}
