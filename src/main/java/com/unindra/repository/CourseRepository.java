package com.unindra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unindra.entity.Course;
import java.util.List;
import java.util.Optional;

import com.unindra.entity.Classroom;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    List<Course> findByClassroom(Classroom classroom);

    long countByClassroom(Classroom classroom);

    boolean existsByClassroomAndName(Classroom classroom, String name);

    Optional<Course> findByClassroomAndName(Classroom classroom, String name);

    Optional<Course> findByClassroomAndCode(Classroom classroom, String code);

    Optional<Course> findByCode(String code);

    @Query("SELECT c FROM Course c " +
            "WHERE c.id NOT IN (" +
            "SELECT ta.course.id FROM TeachingAssignment ta " +
            "WHERE ta.teacher.id = :teacherId AND ta.section.id = :sectionId)")
    List<Course> findCoursesNotTaughtByTeacherInSection(@Param("teacherId") String teacherId,
            @Param("sectionId") String sectionId);

}
