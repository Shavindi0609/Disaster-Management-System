package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.ReportResponseDTO;
import com.ijse.gdse.back_end.service.ReportResponseService;
import com.ijse.gdse.back_end.service.impl.ReportResponseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
public class ReportResponseController {

    private final ReportResponseService reportResponseService;

    // Responses by volunteer
    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<APIResponse> getResponsesByVolunteer(@PathVariable Long volunteerId) {
        List<ReportResponseDTO> responses = reportResponseService.getResponsesByVolunteer(volunteerId);
        return ResponseEntity.ok(new APIResponse(200, "Responses fetched successfully", responses));
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity<APIResponse> getResponsesByReport(@PathVariable Long reportId) {
        List<ReportResponseDTO> responses = reportResponseService.getResponsesByReportDTO(reportId);
        return ResponseEntity.ok(new APIResponse(200, "Responses for report fetched", responses));
    }

}
