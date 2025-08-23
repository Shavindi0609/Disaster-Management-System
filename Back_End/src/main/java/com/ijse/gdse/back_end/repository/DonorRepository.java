package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository  extends JpaRepository<Donor, Long> {

}
