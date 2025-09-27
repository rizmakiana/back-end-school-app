package com.unindra.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "assignments")
public class Assignment {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private String description;

    private String uriFile;

    private LocalDateTime dueDate;

    @OneToMany(mappedBy = "assignment")
    private List<Submission> submissions;
    
}