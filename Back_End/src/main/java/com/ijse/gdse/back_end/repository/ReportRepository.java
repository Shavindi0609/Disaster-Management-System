package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


//    List<Report> findByUsername(String username);

     List<Report> findByEmail(String email);

     List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

     List<Report> findByAssignedVolunteer_Id(Long volunteerId);

}
