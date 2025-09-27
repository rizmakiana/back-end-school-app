package com.unindra.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "meeting_numbers")
public class MeetingNumber {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer number;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "teaching_assignment_id")
    private TeachingAssignment teachingAssignment;

    @OneToMany(mappedBy = "meetingNumber")
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "meetingNumber")
    private List<Material> materials;

    
}