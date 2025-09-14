package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.DisasterReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisasterReportRepository extends JpaRepository<DisasterReport, Long> {
}
