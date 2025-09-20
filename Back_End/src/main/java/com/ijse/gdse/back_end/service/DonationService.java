package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.entity.Donation;

import java.util.Map;

public interface DonationService {

    Donation addDonation(DonationDTO donationDTO);

    double getTotalDonations();

    Map<String, Double> getMonthlyDonations();

    double getAvailableBalance();
}
