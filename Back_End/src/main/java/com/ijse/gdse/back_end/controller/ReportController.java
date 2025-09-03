package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.service.ReportService;
import com.ijse.gdse.back_end.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:63342") // frontend run වෙන port එක
@RestController
@RequestMapping("/auth/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final JwtUtil jwtUtil;

    // ================= Add Report =================
    @PostMapping
    public ResponseEntity<APIResponse> addReport(
            @RequestHeader("Authorization") String authHeader, // ✅ Token ගන්න
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam(required = false) String reporterContact,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) MultipartFile photo
    ) throws IOException {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        Report savedReport = reportService.addReportByUser(username, type, description, reporterContact, latitude, longitude, photo);

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
        String username = jwtUtil.extractUsername(token);

        List<Report> reports = reportService.getReportsByUsername(username);
        return ResponseEntity.ok(new APIResponse(200, "My reports fetched successfully", reports));
    }

    // ================= Total Reports =================
    @GetMapping("/total")
    public ResponseEntity<APIResponse> getTotalReports() {
        long total = reportService.getTotalReports();
        return ResponseEntity.ok(new APIResponse(200, "Total reports fetched", total));
    }

    @PutMapping("/{reportId}/assign/{volunteerId}")
    public ResponseEntity<APIResponse> assignVolunteerToReport(
            @PathVariable Long reportId,
            @PathVariable Long volunteerId
    ) {
        Report updatedReport = reportService.assignVolunteer(reportId, volunteerId);
        return ResponseEntity.ok(new APIResponse(200, "Volunteer assigned successfully", updatedReport));
    }

}
