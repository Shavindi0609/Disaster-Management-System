package com.ijse.gdse.back_end.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReportDTO {

    private Long id;
    private String type;
    private String description;
    private String reporterContact;
    private Double latitude;
    private Double longitude;
    private String photoBase64;
    private String createdAt;
    private String assignedVolunteerName;
    private double allocatedDonationAmount;
}
