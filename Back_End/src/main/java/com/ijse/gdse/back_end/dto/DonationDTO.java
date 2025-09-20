package com.ijse.gdse.back_end.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DonationDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String company;

    @NotNull(message = "Donation amount is required")
    private Double donationAmount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @Pattern(
            regexp = "^[0-9]{16}$",
            message = "Card number must be 16 digits"
    )
    private String cardNumber;

    @Size(max = 50, message = "Card holder name must not exceed 50 characters")
    private String cardName;

    @Pattern(
            regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$",
            message = "Expiry must be in MM/YY format"
    )
    private String expiry;

    @Pattern(
            regexp = "^[0-9]{3,4}$",
            message = "CVV must be 3 or 4 digits"
    )
    private String cvv;

    private boolean receiveUpdates;
}
