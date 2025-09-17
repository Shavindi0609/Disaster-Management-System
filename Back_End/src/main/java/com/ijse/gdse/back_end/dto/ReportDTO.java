package com.ijse.gdse.back_end.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReportDTO {

    private String type;
    private String description;
    private String reporterContact;
    private Double latitude;
    private Double longitude;
    private MultipartFile photo; // incoming file
}
