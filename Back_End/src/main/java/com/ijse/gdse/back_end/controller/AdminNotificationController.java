package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.entity.AdminNotification;
import com.ijse.gdse.back_end.repository.AdminNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
@CrossOrigin
public class AdminNotificationController {

    private final AdminNotificationRepository notificationRepo;

    // Fetch all notifications
    @GetMapping
    public ResponseEntity<List<AdminNotification>> getAllNotifications() {
        List<AdminNotification> notifications = notificationRepo.findAll();
        return ResponseEntity.ok(notifications);
    }

    // Optional: fetch by reportId
    @GetMapping("/report/{reportId}")
    public ResponseEntity<List<AdminNotification>> getNotificationsByReport(@PathVariable Long reportId) {
        List<AdminNotification> notifications = notificationRepo.findByReportId(reportId);
        return ResponseEntity.ok(notifications);
    }

    // Optional: fetch unread notifications
    @GetMapping("/unread")
    public ResponseEntity<List<AdminNotification>> getUnreadNotifications() {
        List<AdminNotification> notifications = notificationRepo.findByReadFalse();
        return ResponseEntity.ok(notifications);
    }
}
