package com.unindra.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "districts")
public class District {

    // @Id
    // private String id;
    
    // private String name;

    // @ManyToOne
    // // @JoinColumn(name = "regency_id")
    // @JoinColumn(name = "regency_id", foreignKey = @ForeignKey(name = "fk_regency"))
    // private Regency regency; 
    @Id
    @Column(length = 6)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "regency_id", nullable = false, foreignKey = @ForeignKey(name = "fk_regency"))
    private Regency regency;

    @OneToMany(mappedBy = "districtAddress")
    private List<User> users;
}
