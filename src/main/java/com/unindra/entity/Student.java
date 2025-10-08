package com.unindra.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student extends User{

    @Column(name = "student_id", unique = true)
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "birthplace_id", insertable = false, updatable = false)
    private Regency birthplace;

    @ManyToOne
    @JoinColumn(name = "district_address_id", insertable = false, updatable = false)
    private District districtAddress;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(mappedBy = "student")
    private List<Grade> grades;

    @OneToMany(mappedBy = "student")
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "student")
    private List<Payment> payments;
    
}