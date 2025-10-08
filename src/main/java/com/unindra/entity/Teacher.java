package com.unindra.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "teachers")
public class Teacher extends User {
    
    @OneToOne(mappedBy = "teacher")
    private HomeroomAssignment homeroomAssignment;
    
    @OneToMany(mappedBy = "teacher")
    private List<TeachingAssignment> teachingAssignments;

}