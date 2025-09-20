package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.entity.AdminNotification;

import java.util.List;

public interface AdminNotificationService {

    AdminNotification saveNotification(AdminNotification notification);

    List<AdminNotification> getAllNotifications();

    List<AdminNotification> getNotificationsByReport(Long reportId);

    List<AdminNotification> getUnreadNotifications();
}
