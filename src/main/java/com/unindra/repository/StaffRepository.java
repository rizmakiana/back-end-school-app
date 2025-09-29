package com.unindra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String>{
    
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Staff> findByUsername(String username);
    
    Optional<Staff> findByEmail(String email);
    
    Optional<Staff> findByPhoneNumber(String phoneNumber);
}
