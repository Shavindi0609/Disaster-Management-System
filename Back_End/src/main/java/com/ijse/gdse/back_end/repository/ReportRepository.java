package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // ✅ User reports වලට
    List<Report> findByUsername(String username);

    List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
