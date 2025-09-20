package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.VolunteerDTO;
import com.ijse.gdse.back_end.entity.Volunteer;

import java.util.List;

public interface VolunteerService {

    String registerVolunteer(VolunteerDTO dto);

    Volunteer authenticateVolunteer(String email, String password);


    Volunteer addVolunteer(VolunteerDTO volunteerDTO);

    List<Volunteer> getAllVolunteers();

    Volunteer getVolunteerById(Long id);

    Volunteer updateVolunteer(Long id, VolunteerDTO volunteerDTO);

    void deleteVolunteer(Long id);

    long countVolunteers();

    Volunteer getVolunteerByEmail(String email);
}
