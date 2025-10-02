package com.unindra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Course;
import java.util.List;
import java.util.Optional;

import com.unindra.entity.Classroom;


@Repository
public interface CourseRepository extends JpaRepository<Course, String>{
    
    List<Course> findByClassroom(Classroom classroom);

    long countByClassroom(Classroom classroom);

    boolean existsByClassroomAndName(Classroom classroom, String name);

    Optional<Course> findByClassroomAndName(Classroom classroom, String name);
    
    Optional<Course> findByClassroomAndCode(Classroom classroom, String code);

}
