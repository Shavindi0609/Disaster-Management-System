package com.ijse.gdse.back_end.service;

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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportResponseService {

    private final ReportResponseRepository reportResponseRepository;
    private final ReportRepository reportRepo;
    private final VolunteerRepository volunteerRepo;

    public ReportResponse addResponse(Long reportId, String volunteerEmail, String statusUpdate, MultipartFile photo) throws IOException {
        Report report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        Volunteer volunteer = volunteerRepo.findByEmail(volunteerEmail)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        // check if volunteer is assigned (optional)
        if (report.getAssignedVolunteer() == null || !report.getAssignedVolunteer().getId().equals(volunteer.getId())) {
            throw new RuntimeException("You are not assigned to this report");
        }

        ReportResponse response = ReportResponse.builder()
                .report(report)
                .volunteer(volunteer)
                .statusUpdate(statusUpdate)
                .respondedAt(LocalDateTime.now())
                .photo(photo != null ? photo.getBytes() : null)
                .build();

        return reportResponseRepository.save(response);
    }

    public List<ReportResponse> getResponsesByReport(Long reportId) {
        return reportResponseRepository.findByReportId(reportId);
    }
}
