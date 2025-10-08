package com.unindra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Student;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, String>{
    
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Student> findByUsername(String username);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT MAX(CAST(student_id AS UNSIGNED)) FROM students", nativeQuery = true)
    Optional<String> findMaxStudentId();

}