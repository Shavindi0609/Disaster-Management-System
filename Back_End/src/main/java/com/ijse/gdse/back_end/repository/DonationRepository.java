package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
