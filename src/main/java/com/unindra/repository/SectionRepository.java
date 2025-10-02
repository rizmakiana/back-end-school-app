package com.unindra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Section;
import java.util.Optional;


@Repository
public interface SectionRepository extends JpaRepository<Section, String>{
    
    boolean existsByCode(String code);

    Optional<Section> findByCode(String code);
}
