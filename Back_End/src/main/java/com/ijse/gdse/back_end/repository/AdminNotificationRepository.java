package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.AdminNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {

    List<AdminNotification> findByReportId(Long reportId);

    List<AdminNotification> findByReadFalse(); // Optional: unread notifications
}
