package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:63342")
//@CrossOrigin(origins = "http://localhost:5500") // report.html run වෙන port එක
@RestController
@RequestMapping("/auth/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<APIResponse> addReport(
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam(required = false) String reporterContact,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) MultipartFile photo
    ) throws IOException {

        Report savedReport = reportService.addReport(type, description, reporterContact, latitude, longitude, photo);

        return ResponseEntity.ok(new APIResponse(200, "Report Added Successfully", savedReport));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(new APIResponse(200, "Reports fetched successfully", reports));
    }

    @GetMapping("/total")
    public ResponseEntity<APIResponse> getTotalReports() {
        long total = reportService.getTotalReports();
        return ResponseEntity.ok(new APIResponse(200, "Total reports fetched", total));
    }
}
