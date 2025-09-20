package com.ijse.gdse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
public class ReportDTO {

    private Long id;

    @NotBlank(message = "Disaster type is mandatory")
    private String type;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;

    @NotBlank(message = "Reporter contact is mandatory")
    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$",
            message = "Reporter contact must be a valid phone number"
    )
    private String reporterContact;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    // optional photo (can be null)
    private String photoBase64;

    private String createdAt; // usually set by backend

    private String assignedVolunteerName; // optional

    private double allocatedDonationAmount;
}
