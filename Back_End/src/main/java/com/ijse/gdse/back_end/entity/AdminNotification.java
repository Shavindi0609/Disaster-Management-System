package com.ijse.gdse.back_end.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reportId;

    private String volunteerEmail;

    private String statusUpdate;

    private LocalDateTime respondedAt;

    private LocalDateTime notifiedAt;

    // ✅ Fix 1: Rename the field
    @Column(name = "is_read", nullable = false)
    private boolean read;
}
