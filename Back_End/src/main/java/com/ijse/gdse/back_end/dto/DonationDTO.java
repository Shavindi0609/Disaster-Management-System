package com.ijse.gdse.back_end.dto;


import lombok.Data;

@Data
public class DonationDTO {

    private String name;
    private String email;
    private String company;
    private String donationAmount;
    private String paymentMethod;
    private String cardNumber;
    private String cardName;
    private String expiry;
    private String cvv;
    private boolean receiveUpdates;
}
