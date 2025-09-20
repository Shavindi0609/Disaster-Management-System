package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.DonorDTO;
import com.ijse.gdse.back_end.entity.Donor;

import java.util.List;

public interface DonorService {

    Donor addDonor(DonorDTO donorDTO);
    List<Donor> getAllDonors();
    Donor getDonorById(Long id);
    Donor updateDonor(Long id, DonorDTO donorDTO);
    void deleteDonor(Long id);
    long countDonors();
}
