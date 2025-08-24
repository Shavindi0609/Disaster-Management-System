package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("SELECT COALESCE(SUM(d.donationAmount), 0) FROM Donation d")
    double getTotalDonations();

    @Query(
            value = "SELECT MONTH(created_at) as month, SUM(donation_amount) as total " +
                    "FROM donations " +
                    "WHERE created_at IS NOT NULL " +
                    "GROUP BY MONTH(created_at) " +
                    "ORDER BY month",
            nativeQuery = true
    )
    List<Object[]> getMonthlyDonationTotals();

}
