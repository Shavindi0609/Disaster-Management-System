package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.LastWeekReportDTO;
import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.entity.Report;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {

    Report addReportByUser(String email, String type, String description,
                           String reporterContact, Double latitude, Double longitude, MultipartFile photo) throws IOException;

    List<ReportDTO> getAllReports();

    List<ReportDTO> getReportsByEmail(String email);

    long getTotalReports();

    Report assignVolunteer(Long reportId, Long volunteerId);

    List<Report> getTodayReports();

    List<Report> getReportsByVolunteer(Long volunteerId);

    List<LastWeekReportDTO> getLastWeekReports();

    Report allocateDonationToReport(Long reportId, double amount);

    void deleteReport(Long reportId, String email);

    Report getReportById(Long reportId);
}
