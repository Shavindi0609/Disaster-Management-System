package com.ijse.gdse.back_end.service;


import com.ijse.gdse.back_end.dto.DonorDTO;
import com.ijse.gdse.back_end.entity.Donor;
import com.ijse.gdse.back_end.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;

    // Add Donor
    public Donor addDonor(DonorDTO donorDTO) {
        Donor donor = Donor.builder()
                .name(donorDTO.getName())
                .email(donorDTO.getEmail())
                .amount(donorDTO.getAmount())
                .build();

        return donorRepository.save(donor);
    }

    // Get all Donors
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    // Get Donor by ID
    public Optional<Donor> getDonorById(Long id) {
        return donorRepository.findById(id);
    }

    // Update Donor
    public Donor updateDonor(Long id, DonorDTO donorDTO) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        donor.setName(donorDTO.getName());
        donor.setEmail(donorDTO.getEmail());
        donor.setAmount(donorDTO.getAmount());

        return donorRepository.save(donor);
    }

    // Delete Donor
    public void deleteDonor(Long id) {
        donorRepository.deleteById(id);
    }

    // ✅ Count Volunteers
    public long countDonors() {
        return donorRepository.count();
    }
}
