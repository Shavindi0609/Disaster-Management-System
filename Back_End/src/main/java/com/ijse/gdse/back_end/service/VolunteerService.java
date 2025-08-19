package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.VolunteerDTO;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VolunteerService {


    private final VolunteerRepository volunteerRepository;

    public Volunteer addVolunteer(VolunteerDTO volunteerDTO) {
        // DTO => Entity conversion
        Volunteer volunteer = Volunteer.builder()
                .name(volunteerDTO.getName())
                .email(volunteerDTO.getEmail())
                .phone(volunteerDTO.getPhone())
                .skills(volunteerDTO.getSkills())
                .build();

        // Save to DB
        return volunteerRepository.save(volunteer);
    }
}
