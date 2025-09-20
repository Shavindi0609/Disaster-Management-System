package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.DonorDTO;
import com.ijse.gdse.back_end.entity.Donor;
import com.ijse.gdse.back_end.service.DonorService;
import com.ijse.gdse.back_end.service.impl.DonorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;

    // Add Donor
    @PostMapping
    public ResponseEntity<APIResponse> addDonor(@RequestBody DonorDTO donorDTO) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Donor Added Successfully",
                        donorService.addDonor(donorDTO)
                )
        );
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
}
