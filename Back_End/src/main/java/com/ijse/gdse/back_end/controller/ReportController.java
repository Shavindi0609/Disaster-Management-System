package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/auth/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<APIResponse> addReport(@RequestBody ReportDTO reportDTO) {
        Report savedReport = reportService.addReport(reportDTO);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Report Added Successfully",
                        savedReport
                )
        );
    }

    @GetMapping("/total")
    public ResponseEntity<APIResponse> getTotalReports() {
        long total = reportService.getTotalReports();
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Total Reports fetched successfully",
                        total
                )
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<APIResponse> getMonthlyReports() {
        Map<String, Long> monthlyTotals = reportService.getMonthlyReports();
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Monthly reports fetched successfully",
                        monthlyTotals
                )
        );
    }
}
