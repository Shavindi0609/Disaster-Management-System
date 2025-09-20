package com.ijse.gdse.back_end.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class LastWeekReportDTO {
    private Long id;
    private String type;
    private String description;
    private Double latitude;
    private Double longitude;
    private String reporterContact;
    private String photoBase64; // already base64 string
    private LocalDateTime createdAt; // createdAt converted to LocalDate
    private VolunteerDTO assignedVolunteer; // 🔹 add this
}
