package com.unindra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unindra.entity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, String>{
    
    List<District> findByRegencyId(String regencyId);
}
