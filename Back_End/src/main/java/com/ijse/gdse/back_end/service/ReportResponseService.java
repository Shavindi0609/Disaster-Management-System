
package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.ReportResponseDTO;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.entity.ReportResponse;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.ReportRepository;
import com.ijse.gdse.back_end.repository.ReportResponseRepository;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportResponseService {

    private final ReportResponseRepository reportResponseRepository;
    private final ReportRepository reportRepo;
    private final VolunteerRepository volunteerRepo;

    /**
     * Add a response to a report
     * @param reportId - Report ID
     * @param volunteerEmail - Volunteer email (from token)
     * @param statusUpdate - Status update text
     * @param photo - Optional MultipartFile photo
     * @return saved ReportResponse entity
     */
    public ReportResponse addResponse(Long reportId, String volunteerEmail, String statusUpdate, MultipartFile photo) throws IOException {
        // 1️⃣ Get report
        Report report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // 2️⃣ Get volunteer
        Volunteer volunteer = volunteerRepo.findByEmail(volunteerEmail)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        // 3️⃣ Verify assignment
        if (report.getAssignedVolunteer() == null) {
            throw new RuntimeException("This report is not assigned to any volunteer");
        }
        if (!report.getAssignedVolunteer().getId().equals(volunteer.getId())) {
            throw new RuntimeException("You are not assigned to this report");
        }

        // 4️⃣ Build response
        ReportResponse response = ReportResponse.builder()
                .report(report)
                .volunteer(volunteer)
                .statusUpdate(statusUpdate)
                .respondedAt(LocalDateTime.now())
                .photo(photo != null ? photo.getBytes() : null)
                .build();

        // 5️⃣ Save to DB
        return reportResponseRepository.save(response);
    }

    /**
     * Get all responses of a volunteer
     */
    public List<ReportResponseDTO> getResponsesByVolunteer(Long volunteerId) {
        return reportResponseRepository.findByVolunteerId(volunteerId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ReportResponseDTO> getResponsesByReportDTO(Long reportId) {
        return reportResponseRepository.findByReportId(reportId)
                .stream()
                .map(this::toDTO)
                .toList();
    }


    /**
     * Convert entity → DTO
     */
    private ReportResponseDTO toDTO(ReportResponse response) {
        return ReportResponseDTO.builder()
                .id(response.getId())
                .reportId(response.getReport().getId())
                .statusUpdate(response.getStatusUpdate())
                .respondedAt(response.getRespondedAt())
                .photoBase64(response.getPhoto() != null
                        ? Base64.getEncoder().encodeToString(response.getPhoto())
                        : null)
                .build();
    }

    /**
     * Get responses by report
     */
    public List<ReportResponse> getResponsesByReport(Long reportId) {
        return reportResponseRepository.findByReportId(reportId);
    }
}
