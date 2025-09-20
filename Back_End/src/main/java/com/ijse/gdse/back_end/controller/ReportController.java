package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.*;
import com.ijse.gdse.back_end.entity.AdminNotification;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.entity.ReportResponse;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.AdminNotificationRepository;
import com.ijse.gdse.back_end.repository.ReportRepository;
import com.ijse.gdse.back_end.repository.ReportResponseRepository;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import com.ijse.gdse.back_end.service.*;
import com.ijse.gdse.back_end.service.impl.EmailServiceImpl;
import com.ijse.gdse.back_end.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

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
    private final VolunteerRepository volunteerRepository;
    private final ReportRepository reportRepository;
    private final ReportResponseRepository reportResponseRepository;

    @Autowired
    private AdminNotificationRepository notificationRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


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
        List<ReportDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(new APIResponse(200, "Reports fetched successfully", reports));
    }


    // ================= My Reports (User) =================
//    @GetMapping("/my")
//    public ResponseEntity<APIResponse> getMyReports(@RequestHeader("Authorization") String authHeader) {
//        String token = authHeader.replace("Bearer ", "");
//        String email = jwtUtil.extractUsername(token);
//
//        List<Report> reports = reportService.getReportsByEmail(email);
//        return ResponseEntity.ok(new APIResponse(200, "My reports fetched successfully", reports));
//    }

    @GetMapping("/my")
    public ResponseEntity<APIResponse> getMyReports(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);

        List<ReportDTO> reports = reportService.getReportsByEmail(email);
        return ResponseEntity.ok(new APIResponse(200, "My reports fetched successfully", reports));
    }


    private ReportDTO mapToDTO(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setType(report.getType());
        dto.setDescription(report.getDescription());
        dto.setReporterContact(report.getReporterContact());
        dto.setLatitude(report.getLatitude());
        dto.setLongitude(report.getLongitude());
        dto.setCreatedAt(report.getCreatedAt().toString());
        dto.setAllocatedDonationAmount(report.getAllocatedDonationAmount());

        if (report.getAssignedVolunteer() != null) {
            dto.setAssignedVolunteerName(report.getAssignedVolunteer().getName());
        }

        // 🔹 Convert photo byte[] → Base64 string
        if (report.getPhoto() != null) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(report.getPhoto()));
        }

        return dto;
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

//    @PostMapping("/{reportId}/respond")
//    public ResponseEntity<APIResponse> respondToReport(
//            @PathVariable Long reportId,
//            @RequestHeader("Authorization") String authHeader,
//            @RequestParam String statusUpdate,
//            @RequestParam(required = false) MultipartFile photo
//    ) throws IOException {
//
//        String token = authHeader.replace("Bearer ", "");
//        String email = jwtUtil.extractUsername(token);
//
//        ReportResponse response = reportResponseService.addResponse(reportId, email, statusUpdate, photo);
//
//        return ResponseEntity.ok(new APIResponse(200, "Response submitted successfully", response));
//    }

//    @PostMapping("/{reportId}/respond")
//    public ResponseEntity<?> addResponse(
//            @PathVariable Long reportId,
//            @RequestParam String statusUpdate,
//            @RequestParam(required = false) MultipartFile photo,
//            Authentication authentication
//    ) throws IOException {
//
//        // 1. Find logged-in volunteer from JWT
//        String email = authentication.getName();
//        Volunteer volunteer = volunteerRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
//
//        // 2. Get the report
//        Report report = reportRepository.findById(reportId)
//                .orElseThrow(() -> new RuntimeException("Report not found"));
//
//        // 3. Check if this report is assigned to the volunteer
//        if (!report.getAssignedVolunteer().getId().equals(volunteer.getId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("You are not allowed to respond to this report");
//        }
//
//        // 4. Create and save response
//        ReportResponse response = ReportResponse.builder()
//                .report(report)
//                .volunteer(volunteer)
//                .statusUpdate(statusUpdate)
//                .respondedAt(LocalDateTime.now())
//                .photo(photo != null ? photo.getBytes() : null)
//                .build();
//
//        reportResponseRepository.save(response);
//
//        return ResponseEntity.ok("Response saved successfully!");
//    }
@PostMapping("/{reportId}/respond")
public ResponseEntity<?> submitResponse(
        @PathVariable Long reportId,
        @RequestParam String statusUpdate,
        @RequestParam(required = false) MultipartFile photo
) throws IOException {

    // 1️⃣ Fetch Report
    Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

    // 2️⃣ Get logged-in user's email from SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName(); // JWT filter එකෙන් set කල email

    // 3️⃣ Fetch Volunteer
    Volunteer volunteer = volunteerRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Volunteer not found"));

    // 4️⃣ Save ReportResponse
    ReportResponse response = ReportResponse.builder()
            .report(report)
            .volunteer(volunteer)
            .statusUpdate(statusUpdate)
            .respondedAt(LocalDateTime.now())
            .photo(photo != null ? photo.getBytes() : null)
            .build();
    reportResponseRepository.save(response);

    // 5️⃣ Save AdminNotification
    AdminNotification notification = AdminNotification.builder()
            .reportId(report.getId())
            .volunteerEmail(volunteer.getEmail())
            .statusUpdate(statusUpdate)
            .respondedAt(response.getRespondedAt())
            .notifiedAt(LocalDateTime.now())
            .build();
    notificationRepo.save(notification);

// 6️⃣ Send WebSocket notification
    System.out.println("Sending notification: " + notification);


    // 6️⃣ Send WebSocket notification
    messagingTemplate.convertAndSend("/topic/adminNotifications", notification);

    return ResponseEntity.ok(response);
}


//    // Volunteer submitting a response
//    @PostMapping("/{reportId}/respond")
//    public ResponseEntity<APIResponse> respondToReport(
//            @PathVariable Long reportId,
//            @RequestParam String statusUpdate,
//            @RequestParam(required = false) MultipartFile photo,
//            @AuthenticationPrincipal UserDetails userDetails // token වලින් volunteer email ගන්න
//    ) throws IOException {
//        String volunteerEmail = userDetails.getUsername(); // token එකෙන් email
//        var response = reportResponseService.addResponse(reportId, volunteerEmail, statusUpdate, photo);
//        System.out.println(response);
//
//        return ResponseEntity.ok(new APIResponse(200, "Response submitted successfully", response.getId()));
//    }

//    @PostMapping("/{reportId}/respond")
//    public APIResponse respondToReport(
//            @PathVariable Long reportId,
//            @RequestHeader("Authorization") String authHeader,
//            @RequestParam String statusUpdate,
//            @RequestParam(required = false) MultipartFile photo
//    ) throws Exception {
//
//        String token = authHeader.replace("Bearer ", "");
//        String email = jwtUtil.extractUsername(token);
//
//        ReportResponse response = reportResponseService.addResponse(reportId, email, statusUpdate, photo);
//
//        // Send real-time notification to admin
//        NotificationDTO notification = new NotificationDTO(
//                reportId,
//                response.getVolunteer().getName(),
//                statusUpdate
//        );
//        messagingTemplate.convertAndSend("/topic/admin-notifications", notification);
//
//        return new APIResponse(200, "Response submitted successfully", response);
//    }
//


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

    // Allocate donations to a report (by admin)
    @PutMapping("/{reportId}/allocate")
    public ResponseEntity<Report> allocateDonation(
            @PathVariable Long reportId,
            @RequestParam double amount) {

        Report updated = reportService.allocateDonationToReport(reportId, amount);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<APIResponse> getReportById(
            @PathVariable Long reportId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        Report report = reportService.getReportById(reportId);

        // check ownership
        if (!report.getEmail().equals(email)) {
            return ResponseEntity.status(403)
                    .body(new APIResponse(403, "Access denied", null));
        }

        // map to DTO
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setType(report.getType());
        dto.setDescription(report.getDescription());
        dto.setReporterContact(report.getReporterContact());
        dto.setLatitude(report.getLatitude());
        dto.setLongitude(report.getLongitude());
        dto.setCreatedAt(report.getCreatedAt().toString());
        dto.setAllocatedDonationAmount(report.getAllocatedDonationAmount());

        if (report.getAssignedVolunteer() != null) {
            dto.setAssignedVolunteerName(report.getAssignedVolunteer().getName());
        }

        if (report.getPhoto() != null) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(report.getPhoto()));
        }

        return ResponseEntity.ok(new APIResponse(200, "Report fetched successfully", dto));
    }


    // ================= Delete Report =================
    @DeleteMapping("/{reportId}")
    public ResponseEntity<APIResponse> deleteReport(
            @PathVariable Long reportId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        reportService.deleteReport(reportId, email);
        return ResponseEntity.ok(new APIResponse(200, "Report deleted successfully", null));
    }





}