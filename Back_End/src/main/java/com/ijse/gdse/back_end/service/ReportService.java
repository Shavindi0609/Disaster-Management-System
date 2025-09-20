package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.*;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.DonationRepository;
import com.ijse.gdse.back_end.repository.ReportRepository;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final DonationRepository donationRepository;

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

    public List<ReportDTO> getAllReports() {
        List<Report> reports = reportRepository.findAll();

        return reports.stream().map(r -> {
            ReportDTO dto = new ReportDTO();
            dto.setId(r.getId());
            dto.setType(r.getType());
            dto.setDescription(r.getDescription());
            dto.setReporterContact(r.getReporterContact());
            dto.setLatitude(r.getLatitude());
            dto.setLongitude(r.getLongitude());
            dto.setAllocatedDonationAmount(r.getAllocatedDonationAmount());
            dto.setCreatedAt(r.getCreatedAt().toString());
            dto.setAssignedVolunteerName(r.getAssignedVolunteer() != null ? r.getAssignedVolunteer().getName() : "Not Assigned");

            if (r.getPhoto() != null) {
                dto.setPhotoBase64(Base64.getEncoder().encodeToString(r.getPhoto()));
            }

            return dto;
        }).toList();
    }


    // Fetch user reports
    public List<Report> getReportsByEmail(String email) {
        return reportRepository.findByEmail(email);
    }

    // Fetch user reports
//    public List<ReportDTO> getReportsByEmail(String email) {
//        return reportRepository.findByEmail(email)
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }

    private ReportDTO mapToDTO(Report r) {
        ReportDTO dto = new ReportDTO();
        dto.setId(r.getId());
        dto.setType(r.getType());
        dto.setDescription(r.getDescription());
        dto.setReporterContact(r.getReporterContact());
        dto.setLatitude(r.getLatitude());
        dto.setLongitude(r.getLongitude());
        dto.setAllocatedDonationAmount(r.getAllocatedDonationAmount());
        dto.setCreatedAt(r.getCreatedAt().toString());
        dto.setAssignedVolunteerName(
                r.getAssignedVolunteer() != null ? r.getAssignedVolunteer().getName() : "Not Assigned"
        );

        if (r.getPhoto() != null) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(r.getPhoto()));
        }

        return dto;
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
                            r.getCreatedAt(),
                            volunteerDTO
                    );
                })
                .toList();
    }
//
//    // Admin allocates some donation amount to a report
//    public Report allocateDonationToReport(Long reportId, double amount) {
//        Report report = reportRepository.findById(reportId)
//                .orElseThrow(() -> new RuntimeException("Report not found"));
//
//        // Increase allocated donation
//        report.setAllocatedDonationAmount(report.getAllocatedDonationAmount() + amount);
//
//        return reportRepository.save(report);
//    }

    // Admin allocates some donation amount to a report
    @Transactional
    public Report allocateDonationToReport(Long reportId, double amount) {
        // 1️⃣ Find the report
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // 2️⃣ Get total available donation balance
        double totalAvailable = donationRepository.getTotalBalance();
        if (amount > totalAvailable) {
            throw new RuntimeException("Not enough funds to allocate");
        }

        // 3️⃣ Allocate to report
        report.setAllocatedDonationAmount(report.getAllocatedDonationAmount() + amount);
        reportRepository.save(report);

        // 4️⃣ Deduct from donations (FIFO)
        List<Donation> donations = donationRepository.findDonationsWithBalance(); // oldest first
        double remaining = amount;

        for (Donation d : donations) {
            if (remaining <= 0) break;

            double deduct = Math.min(d.getBalance(), remaining);
            d.setBalance(d.getBalance() - deduct);
            donationRepository.save(d);
            remaining -= deduct;
        }

        // 5️⃣ Optional audit/log entry (no negative amount!)
//        Donation allocationRecord = new Donation();
//        allocationRecord.setDonationAmount(0); // info only
//        allocationRecord.setBalance(0);
//        allocationRecord.setName("System Allocation");
//        allocationRecord.setEmail("system@allocation");
//        allocationRecord.setCompany("N/A");
//        allocationRecord.setPaymentMethod("N/A");
//        allocationRecord.setReport(report);
//        allocationRecord.setCreatedAt(LocalDateTime.now());

//        donationRepository.save(allocationRecord);

        return report;
    }



    // ================= Delete Report =================
    public void deleteReport(Long reportId, String email) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // Ensure only the owner can delete
        if (!report.getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this report");
        }

        reportRepository.delete(report);
    }

    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

}