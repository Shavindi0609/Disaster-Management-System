package com.ijse.gdse.back_end.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String description;
    private String reporterContact;

    private Double latitude;
    private Double longitude;

    private String photoPath; // store file path

    @CreationTimestamp
    private LocalDateTime createdAt;

    // ✅ Add this field to track which user created the report
    private String email;

    // ✅ Assigned volunteer
    @ManyToOne
    @JoinColumn(name = "assigned_volunteer_id")
    private Volunteer assignedVolunteer;
}
