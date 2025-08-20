package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth/donations")
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
}
