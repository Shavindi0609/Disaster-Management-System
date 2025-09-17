package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.LastWeekReportDTO;
import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.dto.VolunteerDTO;
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
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Base64;
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

//    // Report add without user (existing)
//    public Report addReport(String type, String description, String reporterContact,
//                            Double latitude, Double longitude, MultipartFile photo) throws IOException {
//
//        Report report = new Report();
//        report.setType(type);
//        report.setDescription(description);
//        report.setReporterContact(reporterContact);
//        report.setLatitude(latitude);
//        report.setLongitude(longitude);
//
//        if (photo != null && !photo.isEmpty()) {
//            File uploadDir = new File(UPLOAD_DIR);
//            if (!uploadDir.exists()) uploadDir.mkdirs();
//
//            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
//            File file = new File(uploadDir, fileName);
//            photo.transferTo(file);
//            report.setPhotoPath(file.getAbsolutePath());
//        }
//
//        return reportRepository.save(report);
//    }

    // Report add with user (when logged in)
    public Report addReportByUser(String email, String type, String description,
                                  String reporterContact, Double latitude, Double longitude, MultipartFile photo) throws IOException {
        Report report = new Report();
        report.setType(type);
        report.setDescription(description);
        report.setReporterContact(reporterContact);
        report.setLatitude(latitude);
        report.setLongitude(longitude);
        report.setEmail(email);

        if (photo != null && !photo.isEmpty()) {
            // ✅ convert file → byte[] and save in DB
            report.setPhoto(photo.getBytes());
        }

        return reportRepository.save(report);
    }


    // Fetch all reports (Admin)
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Fetch user reports
    public List<Report> getReportsByEmail(String email) {
        return reportRepository.findByEmail(email);
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

    public List<Report> getTodayReports() {
        LocalDate today = LocalDate.now();
        return reportRepository.findByCreatedAtBetween(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );
    }

    public List<Report> getReportsByVolunteer(Long volunteerId) {
        return reportRepository.findByAssignedVolunteer_Id(volunteerId);
    }


    public List<LastWeekReportDTO> getLastWeekReports() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Report> reports = reportRepository.findByCreatedAtAfter(sevenDaysAgo);

        return reports.stream()
                .map(r -> {
                    String photoBase64 = null;
                    if (r.getPhoto() != null) {
                        photoBase64 = Base64.getEncoder().encodeToString(r.getPhoto());
                    }

                    VolunteerDTO volunteerDTO = null;
                    if (r.getAssignedVolunteer() != null) {
                        volunteerDTO = new VolunteerDTO();
                        volunteerDTO.setName(r.getAssignedVolunteer().getName());
                        volunteerDTO.setEmail(r.getAssignedVolunteer().getEmail());
                        volunteerDTO.setPhone(r.getAssignedVolunteer().getPhone());
                        volunteerDTO.setSkills(r.getAssignedVolunteer().getSkills());
                    }

                    return new LastWeekReportDTO(
                            r.getId(),
                            r.getType(),
                            r.getDescription(),
                            r.getLatitude(),
                            r.getLongitude(),
                            r.getReporterContact(),
                            photoBase64,
                            r.getCreatedAt().toLocalDate(),
                            volunteerDTO
                    );
                })
                .toList();
    }



}