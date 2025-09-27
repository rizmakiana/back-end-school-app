package com.unindra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "staffs")
public class Staff extends User{
    
    @ManyToOne
    @JoinColumn(name = "birthplace_id", insertable = false, updatable = false)
    private Regency birthplace;

    @ManyToOne
    @JoinColumn(name = "district_address_id", insertable = false, updatable = false)
    private District districtAddress;

}