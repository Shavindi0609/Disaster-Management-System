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

    public Donation addDonationToReport(Long reportId, DonationDTO dto) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        Donation donation = Donation.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .company(dto.getCompany())
                .donationAmount(dto.getDonationAmount())
                .paymentMethod(dto.getPaymentMethod())
                .cardNumber(dto.getCardNumber())
                .cardName(dto.getCardName())
                .expiry(dto.getExpiry())
                .cvv(dto.getCvv())
                .receiveUpdates(dto.isReceiveUpdates())
                .report(report) // attach to report
                .build();

        return donationRepository.save(donation);
    }

//    // Get total donations across all reports
//    public double getTotalDonations() {
//        return donationRepository.findAll()
//                .stream()
//                .mapToDouble(Donation::getDonationAmount)
//                .sum();
//    }
}
