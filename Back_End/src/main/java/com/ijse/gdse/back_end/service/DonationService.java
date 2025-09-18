package com.ijse.gdse.back_end.service;


import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.repository.DonationRepository;
import com.ijse.gdse.back_end.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final ReportRepository reportRepository;

    public Donation addDonation(DonationDTO donationDTO) {
        // DTO => Entity conversion
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

        // Save to DB
        return donationRepository.save(donation);
    }

    // 🔹 New method for total donations
    public double getTotalDonations() {
        return donationRepository.getTotalDonations();
    }


    public Map<String, Double> getMonthlyDonations() {
        List<Object[]> results = donationRepository.getMonthlyDonationTotals();

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        Map<String, Double> monthlyTotals = new LinkedHashMap<>();

        // initialize all months to 0
        for (String month : months) {
            monthlyTotals.put(month, 0.0);
        }

        for (Object[] row : results) {
            if (row[0] == null || row[1] == null) continue; // null-safe
            Integer monthIndex = ((Number) row[0]).intValue();
            Double total = ((Number) row[1]).doubleValue();
            monthlyTotals.put(months[monthIndex - 1], total);
        }

        return monthlyTotals;
    }

//    public Donation addDonationToReport(Long reportId, DonationDTO dto) {
//        Report report = reportRepository.findById(reportId)
//                .orElseThrow(() -> new RuntimeException("Report not found"));
//
//        Donation donation = Donation.builder()
//                .donationAmount(dto.getDonationAmount())
//                .paymentMethod("Admin Assigned") // හෝ dto.paymentMethod
//                .report(report)
//                .build();
//
//        return donationRepository.save(donation);
//    }

    @Transactional
    public Donation addDonationToReport(Long reportId, Donation donation) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));

        // Link donation to report
        donation.setReport(report);

        // Save donation
        Donation savedDonation = donationRepository.save(donation);

        // Optional: update report total if you have a field, otherwise use getTotalDonations() method
        // double total = report.getTotalDonations();

        return savedDonation;
    }
}
