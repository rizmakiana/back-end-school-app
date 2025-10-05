package com.unindra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Teacher;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String>{
    
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Teacher> findByUsername(String username);

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByPhoneNumber(String phoneNumber);
}
