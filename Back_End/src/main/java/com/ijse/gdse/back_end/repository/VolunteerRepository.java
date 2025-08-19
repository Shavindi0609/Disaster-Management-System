package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
