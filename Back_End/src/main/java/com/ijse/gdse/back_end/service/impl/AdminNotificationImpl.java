package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.entity.AdminNotification;
import com.ijse.gdse.back_end.repository.AdminNotificationRepository;
import com.ijse.gdse.back_end.service.AdminNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNotificationImpl implements AdminNotificationService {

    private final AdminNotificationRepository notificationRepo;

    @Override
    public AdminNotification saveNotification(AdminNotification notification) {
        return notificationRepo.save(notification);
    }

    @Override
    public List<AdminNotification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    @Override
    public List<AdminNotification> getNotificationsByReport(Long reportId) {
        return notificationRepo.findByReportId(reportId);
    }

    @Override
    public List<AdminNotification> getUnreadNotifications() {
        return List.of();
    }

    @Override
    public AdminNotification markAsRead(Long id) {
        return notificationRepo.findById(id)
                .map(notification -> {
                    notification.setRead(true);
                    return notificationRepo.save(notification);
                }).orElse(null);
    }

}
