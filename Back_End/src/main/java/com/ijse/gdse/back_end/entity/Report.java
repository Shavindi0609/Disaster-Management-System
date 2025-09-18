package com.ijse.gdse.back_end.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

//    private String photoPath; // store file path

    // ✅ Store image as binary in DB
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // ✅ Add this field to track which user created the report
    private String email;

    // ✅ Assigned volunteer
    @ManyToOne
    @JoinColumn(name = "assigned_volunteer_id")
    private Volunteer assignedVolunteer;


    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Donation> donations;

//    // 🔹 Add donation list
//    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Donation> donations;


//
//    // 🔹 Helper method for total donation amount
//    public double getTotalDonations() {
//        if (donations == null) return 0;
//        return donations.stream().mapToDouble(Donation::getDonationAmount).sum();
//    }
}
