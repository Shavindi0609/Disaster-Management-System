package com.ijse.gdse.back_end.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AdminNotificationDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reportId;
    private String volunteerEmail;
    private String statusUpdate;
    private LocalDateTime respondedAt;
    private LocalDateTime notifiedAt;

}
