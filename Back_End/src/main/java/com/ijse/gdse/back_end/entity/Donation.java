package com.ijse.gdse.back_end.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
