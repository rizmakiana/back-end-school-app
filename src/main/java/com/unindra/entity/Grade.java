package com.unindra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grades")
public class Grade {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private Double taskScore;

    private Double midTestScore;

    private Double lastTestScore;

    @ManyToOne
    @JoinColumn(name = "teaching_assignment_id")
    private TeachingAssignment teachingAssignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
}