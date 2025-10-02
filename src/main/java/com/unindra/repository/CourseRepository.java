package com.unindra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>{
    
}
