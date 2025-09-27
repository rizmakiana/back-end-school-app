package com.unindra.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "birthplace_id", insertable = false, updatable = false)
    private Regency birthplace;

    @ManyToOne
    @JoinColumn(name = "district_address_id", insertable = false, updatable = false)
    private District districtAddress;
    
    @OneToOne(mappedBy = "teacher")
    private HomeroomAssignment homeroomAssignment;
    
    @OneToMany(mappedBy = "teacher")
    private List<TeachingAssignment> teachingAssignments;

}