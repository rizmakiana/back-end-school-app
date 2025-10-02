package com.unindra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Course;
import java.util.List;
import com.unindra.entity.Classroom;


@Repository
public interface CourseRepository extends JpaRepository<Course, String>{
    
    List<Course> findByClassroom(Classroom classroom);

    long countByClassroom(Classroom classroom);

    boolean existsByClassroomAndName(Classroom classroom, String name);

}
