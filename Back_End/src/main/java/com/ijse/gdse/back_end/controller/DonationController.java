package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.service.DonationService;
import com.ijse.gdse.back_end.service.impl.DonationServiceImpl;
import jakarta.validation.Valid;
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
    public ResponseEntity<APIResponse> addDonation(@RequestBody @Valid DonationDTO donationDTO) {
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

    @GetMapping("/available-balance")
    public ResponseEntity<APIResponse> getAvailableBalance() {
        double balance = donationService.getAvailableBalance();
        return ResponseEntity.ok(new APIResponse(200, "Available balance fetched successfully", balance));
    }



    @GetMapping("/monthly")
    public ResponseEntity<APIResponse> getMonthlyDonations() {
        Map<String, Double> monthlyTotals = donationService.getMonthlyDonations();
        return ResponseEntity.ok(new APIResponse(200, "Monthly donations fetched successfully", monthlyTotals));
    }

//    // Add donation to a specific report
//    @PostMapping("/{reportId}/donations")
//    public ResponseEntity<?> addDonation(@PathVariable Long reportId,
//                                         @RequestBody Donation donation) {
//        Donation saved = donationService.addDonationToReport(reportId, donation);
//        return ResponseEntity.ok(saved);
//    }

//    // Get total donations (for dashboard)
//    @GetMapping("/donations/total")
//    public ResponseEntity<Double> getTotalDonations() {
//        return ResponseEntity.ok(donationService.getTotalDonations());
//    }

}
