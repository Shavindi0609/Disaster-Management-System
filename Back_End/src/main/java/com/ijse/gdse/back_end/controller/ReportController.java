package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.dto.LastWeekReportDTO;
import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.entity.ReportResponse;
import com.ijse.gdse.back_end.service.DonationService;
import com.ijse.gdse.back_end.service.EmailService;
import com.ijse.gdse.back_end.service.ReportResponseService;
import com.ijse.gdse.back_end.service.ReportService;
import com.ijse.gdse.back_end.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;

    private final ReportResponseService reportResponseService;
    private final DonationService donationService;

    // ================= Add Report =================
    @PostMapping
    public ResponseEntity<APIResponse> addReport(
            Authentication authentication,
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam(required = false) String reporterContact,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) MultipartFile photo
    ) throws IOException {
        String email = authentication.getName(); // JWT එකෙන් email ස්වයංක්‍රීයව ලබා ගනී

        Report savedReport = reportService.addReportByUser(
                email, type, description, reporterContact, latitude, longitude, photo
        );

        return ResponseEntity.ok(new APIResponse(200, "Report Added Successfully", savedReport));
    }
    // ================= All Reports (Admin) =================
    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(new APIResponse(200, "Reports fetched successfully", reports));
    }

    // ================= My Reports (User) =================
    @GetMapping("/my")
    public ResponseEntity<APIResponse> getMyReports(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);

        List<Report> reports = reportService.getReportsByEmail(email);
        return ResponseEntity.ok(new APIResponse(200, "My reports fetched successfully", reports));
    }

    // ================= Total Reports =================
    @GetMapping("/total")
    public ResponseEntity<APIResponse> getTotalReports() {
        long total = reportService.getTotalReports();
        return ResponseEntity.ok(new APIResponse(200, "Total reports fetched", total));
    }

//    @PutMapping("/{reportId}/assign/{volunteerId}")
//    public ResponseEntity<APIResponse> assignVolunteerToReport(
//            @PathVariable Long reportId,
//            @PathVariable Long volunteerId
//    ) {
//        Report updatedReport = reportService.assignVolunteer(reportId, volunteerId);
//        return ResponseEntity.ok(new APIResponse(200, "Volunteer assigned successfully", updatedReport));
//    }

    @PutMapping("/{reportId}/assign/{volunteerId}")
    public ResponseEntity<APIResponse> assignVolunteer(
            @PathVariable Long reportId,
            @PathVariable Long volunteerId) {

        Report report = reportService.assignVolunteer(reportId, volunteerId);

        // Send email
        if (report.getAssignedVolunteer() != null) {
            String email = report.getAssignedVolunteer().getEmail();
            emailService.sendVolunteerAssignmentEmail(email, report.getType(), report.getDescription());
        }

        return ResponseEntity.ok(new APIResponse(200, "Volunteer assigned successfully", report));
    }

    // ================= Today's Reports =================
    @GetMapping("/today")
    public ResponseEntity<APIResponse> getTodayReports() {
        List<Report> todayReports = reportService.getTodayReports();
        return ResponseEntity.ok(new APIResponse(200, "Today's reports fetched", todayReports));
    }

    // 🔹 Get reports assigned to a volunteer
    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<APIResponse> getReportsByVolunteer(@PathVariable Long volunteerId) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Reports for volunteer fetched successfully",
                        reportService.getReportsByVolunteer(volunteerId)
                )
        );
    }

    @PostMapping("/{reportId}/respond")
    public ResponseEntity<APIResponse> respondToReport(
            @PathVariable Long reportId,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String statusUpdate,
            @RequestParam(required = false) MultipartFile photo
    ) throws IOException {

        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);

        ReportResponse response = reportResponseService.addResponse(reportId, email, statusUpdate, photo);

        return ResponseEntity.ok(new APIResponse(200, "Response submitted successfully", response));
    }



    @GetMapping("/volunteer/{volunteerId}/count")
    public ResponseEntity<APIResponse> getReportCountByVolunteer(@PathVariable Long volunteerId) {
        long count = reportService.getReportsByVolunteer(volunteerId).size();
        return ResponseEntity.ok(new APIResponse(200, "Report count fetched successfully", count));
    }

    @GetMapping("/last-week")
    public ResponseEntity<APIResponse> getLastWeekReports() {
        List<LastWeekReportDTO> reports = reportService.getLastWeekReports();
        return ResponseEntity.ok(new APIResponse(200, "Last week reports fetched", reports));
    }

//    @PostMapping("/{reportId}/donations")
//    public ResponseEntity<APIResponse> addDonationToReport(
//            @PathVariable Long reportId,
//            @RequestBody DonationDTO dto
//    ) {
//        Donation donation = donationService.addDonationToReport(reportId, dto);
//
//        return ResponseEntity.ok(new APIResponse(
//                200,
//                "Donation added successfully",
//                donation
//        ));
//    }




}