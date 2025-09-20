package com.ijse.gdse.back_end.service.impl;


import com.ijse.gdse.back_end.dto.DonorDTO;
import com.ijse.gdse.back_end.entity.Donor;
import com.ijse.gdse.back_end.repository.DonorRepository;
import com.ijse.gdse.back_end.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;

    @Override
    public Donor addDonor(DonorDTO donorDTO) {
        Donor donor = Donor.builder()
                .name(donorDTO.getName())
                .email(donorDTO.getEmail())
                .amount(donorDTO.getAmount())
                .build();
        return donorRepository.save(donor);
    }

    @Override
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Override
    public Donor getDonorById(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
    }

    @Override
    public Donor updateDonor(Long id, DonorDTO donorDTO) {
        Donor donor = getDonorById(id);
        donor.setName(donorDTO.getName());
        donor.setEmail(donorDTO.getEmail());
        donor.setAmount(donorDTO.getAmount());
        return donorRepository.save(donor);
    }

    @Override
    public void deleteDonor(Long id) {
        Donor donor = getDonorById(id);
        donorRepository.delete(donor);
    }

    @Override
    public long countDonors() {
        return donorRepository.count();
    }
}
