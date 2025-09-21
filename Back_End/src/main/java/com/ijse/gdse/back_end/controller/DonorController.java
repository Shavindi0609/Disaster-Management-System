package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.DonorDTO;
import com.ijse.gdse.back_end.entity.Donor;
import com.ijse.gdse.back_end.service.DonorService;
import com.ijse.gdse.back_end.service.impl.DonorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
@Slf4j
public class DonorController {

    private final DonorService donorService;

    // Add Donor
    @PostMapping
    public ResponseEntity<APIResponse> addDonor(@Valid @RequestBody DonorDTO donorDTO) {
        log.info("INFO - Adding new donor: {}", donorDTO.getEmail());
        log.debug("DEBUG - Donor details: {}", donorDTO);
        log.warn("WARN - Ensure donor email {} is unique", donorDTO.getEmail());

        try {
            Object savedDonor = donorService.addDonor(donorDTO);
            log.info("INFO - Donor {} added successfully", donorDTO.getEmail());

            return ResponseEntity.ok(
                    new APIResponse(
                            200,
                            "Donor Added Successfully",
                            savedDonor
                    )
            );
        } catch (Exception e) {
            log.error("ERROR - Failed to add donor {}. Reason: {}", donorDTO.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    new APIResponse(
                            500,
                            "Failed to add donor",
                            null
                    )
            );
        }
    }

    // Get All Donors
    @GetMapping
    public ResponseEntity<APIResponse> getAllDonors() {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Donors fetched successfully",
                        donorService.getAllDonors()
                )
        );
    }

    // Get Donor by ID
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getDonorById(@PathVariable Long id) {
        Donor donor = donorService.getDonorById(id); // will throw exception if not found
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Donor fetched successfully",
                        donor
                )
        );
    }


    // Update Donor
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateDonor(@PathVariable Long id, @RequestBody DonorDTO donorDTO) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Donor updated successfully",
                        donorService.updateDonor(id, donorDTO)
                )
        );
    }

    // Delete Donor
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteDonor(@PathVariable Long id) {
        donorService.deleteDonor(id);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Donor deleted successfully",
                        null
                )
        );
    }

    @GetMapping("/count")
    public ResponseEntity<APIResponse> getDonorCount() {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Donor count fetched successfully",
                        donorService.countDonors()
                )
        );
    }


    // Search donors by keyword (name or email)
    @GetMapping("/search/{keyword}")
    public ResponseEntity<APIResponse> searchDonors(@PathVariable String keyword) {
        try {
            List<DonorDTO> results = donorService.searchDonorsByKeyword(keyword); // service layer implement කරන්න
            return ResponseEntity.ok(new APIResponse(200, "Donors fetched successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new APIResponse(500, "Failed to search donors", null));
        }
    }


}
