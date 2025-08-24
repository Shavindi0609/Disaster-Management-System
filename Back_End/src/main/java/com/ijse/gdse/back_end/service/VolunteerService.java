package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.VolunteerDTO;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // Get all volunteers
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    // Get volunteer by ID
    public Optional<Volunteer> getVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }

    // ✅ Update Volunteer
    public Volunteer updateVolunteer(Long id, VolunteerDTO volunteerDTO) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        volunteer.setName(volunteerDTO.getName());
        volunteer.setEmail(volunteerDTO.getEmail());
        volunteer.setPhone(volunteerDTO.getPhone());
        volunteer.setSkills(volunteerDTO.getSkills());

        return volunteerRepository.save(volunteer);
    }

    // ✅ Delete Volunteer
    public void deleteVolunteer(Long id) {
        volunteerRepository.deleteById(id);
    }
}
