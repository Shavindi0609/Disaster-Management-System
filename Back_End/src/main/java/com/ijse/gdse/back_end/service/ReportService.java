package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.ReportRepository;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final VolunteerRepository volunteerRepository;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    // Report add without user (existing)
    public Report addReport(String type, String description, String reporterContact,
                            Double latitude, Double longitude, MultipartFile photo) throws IOException {

        Report report = new Report();
        report.setType(type);
        report.setDescription(description);
        report.setReporterContact(reporterContact);
        report.setLatitude(latitude);
        report.setLongitude(longitude);

        if (photo != null && !photo.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            File file = new File(uploadDir, fileName);
            photo.transferTo(file);
            report.setPhotoPath(file.getAbsolutePath());
        }

        return reportRepository.save(report);
    }

    // Report add with user (when logged in)
    public Report addReportByUser(String username, String type, String description,
                                  String reporterContact, Double latitude, Double longitude, MultipartFile photo) throws IOException {
        Report report = new Report();
        report.setType(type);
        report.setDescription(description);
        report.setReporterContact(reporterContact);
        report.setLatitude(latitude);
        report.setLongitude(longitude);
        report.setUsername(username);

        if (photo != null && !photo.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            File file = new File(uploadDir, fileName);
            photo.transferTo(file);
            report.setPhotoPath(file.getAbsolutePath());
        }

        return reportRepository.save(report);
    }

    // Fetch all reports (Admin)
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Fetch user reports
    public List<Report> getReportsByUsername(String username) {
        return reportRepository.findByUsername(username);
    }

    // Total reports count
    public long getTotalReports() {
        return reportRepository.count();
    }

    public Report assignVolunteer(Long reportId, Long volunteerId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        report.setAssignedVolunteer(volunteer);
        return reportRepository.save(report);
    }

}