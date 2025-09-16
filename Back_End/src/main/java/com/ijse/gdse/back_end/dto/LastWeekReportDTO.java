package com.ijse.gdse.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastWeekReportDTO {
    private Long id;
    private String type;
    private String description;
    private Double latitude;
    private Double longitude;
    private String reporterContact;
    private String photoPath;
    private LocalDate date; // createdAt converted to LocalDate
}
