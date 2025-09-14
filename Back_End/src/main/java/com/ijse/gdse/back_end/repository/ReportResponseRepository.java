package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.ReportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportResponseRepository extends JpaRepository<ReportResponse, Long> {

    List<ReportResponse> findByReportId(Long reportId);
}
