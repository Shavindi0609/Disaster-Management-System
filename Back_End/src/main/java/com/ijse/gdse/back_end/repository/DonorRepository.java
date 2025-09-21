package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorRepository  extends JpaRepository<Donor, Long> {

    List<Donor> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

}
