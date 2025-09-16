package com.ijse.gdse.back_end.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {

    private Long id;
    private Long reportId;
    private String statusUpdate;
    private LocalDateTime respondedAt;
    private String photoBase64;
}
