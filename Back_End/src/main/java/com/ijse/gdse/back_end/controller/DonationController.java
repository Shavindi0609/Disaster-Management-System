package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<APIResponse> addDonation(@RequestBody DonationDTO donationDTO) {
        Donation savedDonation = donationService.addDonation(donationDTO);

        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Donation Added Successfully",
                        savedDonation
                )
        );
    }

    @GetMapping("/total")
    public ResponseEntity<APIResponse> getTotalDonations() {
        double total = donationService.getTotalDonations();
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Total Donations fetched successfully",
                        total
                )
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<APIResponse> getMonthlyDonations() {
        Map<String, Double> monthlyTotals = donationService.getMonthlyDonations();
        return ResponseEntity.ok(new APIResponse(200, "Monthly donations fetched successfully", monthlyTotals));
    }

    // Add donation to a report
    @PostMapping("/{reportId}/donations")
    public ResponseEntity<?> addDonation(@PathVariable Long reportId, @RequestBody Donation donation) {
        try {
            Donation savedDonation = donationService.addDonationToReport(reportId, donation);
            return ResponseEntity.ok(savedDonation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
