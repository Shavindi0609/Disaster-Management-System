package com.ijse.gdse.back_end.service;


import com.ijse.gdse.back_end.dto.DonationDTO;
import com.ijse.gdse.back_end.entity.Donation;
import com.ijse.gdse.back_end.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;

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
}
