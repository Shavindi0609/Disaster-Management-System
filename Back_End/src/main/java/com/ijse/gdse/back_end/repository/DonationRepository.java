package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("SELECT COALESCE(SUM(d.donationAmount), 0) FROM Donation d")
    double getTotalDonations();

}
