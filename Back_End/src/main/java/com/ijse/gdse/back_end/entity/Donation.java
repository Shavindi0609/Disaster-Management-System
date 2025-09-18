package com.ijse.gdse.back_end.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String company;
    private double donationAmount;
    private String paymentMethod;
    private String cardNumber;
    private String cardName;
    private String expiry;
    private String cvv;
    private boolean receiveUpdates;

    // 🔹 createdAt field එක auto set වෙනවා save වෙද්දි
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = true)
    private Report report;

    private double balance;

    // 🔹 Add this field
    private String description;

    @PrePersist
    public void prePersist() {
        if (balance == 0) {
            balance = donationAmount;
        }
    }

}
