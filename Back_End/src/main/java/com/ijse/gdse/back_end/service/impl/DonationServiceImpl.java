package com.ijse.gdse.back_end.service.impl;


import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.repository.DonationRepository;
import com.ijse.gdse.back_end.repository.ReportRepository;
import com.ijse.gdse.back_end.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final ReportRepository reportRepository;

    @Override
    public Donation addDonation(DonationDTO donationDTO) {
        Donation donation = Donation.builder()
                .name(donationDTO.getName())
                .email(donationDTO.getEmail())
                .company(donationDTO.getCompany())
                .donationAmount(donationDTO.getDonationAmount())
                .paymentMethod(donationDTO.getPaymentMethod())
                .cardNumber(donationDTO.getCardNumber())
                .cardName(donationDTO.getCardName())
                .expiry(donationDTO.getExpiry())
                .cvv(donationDTO.getCvv())
                .receiveUpdates(donationDTO.isReceiveUpdates())
                .build();

        return donationRepository.save(donation);
    }

    @Override
    public double getTotalDonations() {
        Double total = donationRepository.getTotalDonations();
        return total != null ? total : 0.0;
    }

    @Override
    public Map<String, Double> getMonthlyDonations() {
        List<Object[]> results = donationRepository.getMonthlyDonationTotals();

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        Map<String, Double> monthlyTotals = new LinkedHashMap<>();

        // initialize all months to 0
        for (String month : months) {
            monthlyTotals.put(month, 0.0);
        }

        for (Object[] row : results) {
            if (row[0] == null || row[1] == null) continue;
            Integer monthIndex = ((Number) row[0]).intValue();
            Double total = ((Number) row[1]).doubleValue();
            monthlyTotals.put(months[monthIndex - 1], total);
        }

        return monthlyTotals;
    }

    @Override
    @Transactional(readOnly = true)  // 🔹 read-only transaction
    public double getAvailableBalance() {
        Double total = donationRepository.getTotalBalance();
        return total != null ? total : 0.0;
    }

}

