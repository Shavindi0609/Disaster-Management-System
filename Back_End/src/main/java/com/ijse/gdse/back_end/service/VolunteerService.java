package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.VolunteerDTO;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerService {


    private final VolunteerRepository volunteerRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new volunteer
    public String registerVolunteer(VolunteerDTO dto) {
        if (volunteerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Volunteer volunteer = Volunteer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .skills(dto.getSkills())
                .password(passwordEncoder.encode(dto.getPassword()))
                .active(true)
                .build();

        volunteerRepository.save(volunteer);

        return "Volunteer registered successfully";
    }

    // Authenticate volunteer
    public Volunteer authenticateVolunteer(String email, String password, PasswordEncoder encoder) {
        Volunteer volunteer = volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!encoder.matches(password, volunteer.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return volunteer;
    }


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

    //  Update Volunteer
    public Volunteer updateVolunteer(Long id, VolunteerDTO volunteerDTO) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        volunteer.setName(volunteerDTO.getName());
        volunteer.setEmail(volunteerDTO.getEmail());
        volunteer.setPhone(volunteerDTO.getPhone());
        volunteer.setSkills(volunteerDTO.getSkills());

        return volunteerRepository.save(volunteer);
    }

    //  Delete Volunteer
    public void deleteVolunteer(Long id) {
        volunteerRepository.deleteById(id);
    }

    //  Count Volunteers
    public long countVolunteers() {
        return volunteerRepository.count();
    }

//    public VolunteerDTO updateVolunteerStatus(Long id, Boolean active) {
//        Volunteer volunteer = volunteerRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
//        volunteer.setActive(active);
//        Volunteer updated = volunteerRepository.save(volunteer);
//
//        VolunteerDTO dto = new VolunteerDTO();
//        dto.setName(updated.getName());
//        dto.setEmail(updated.getEmail());
//        dto.setPhone(updated.getPhone());
//        dto.setSkills(updated.getSkills());
//        dto.setActive(updated.getActive()); // if you added active to DTO
//
//        return dto;
//    }
//
//    // VolunteerService.java
//    public Optional<VolunteerDTO> getVolunteerByEmail(String email) {
//        Optional<Volunteer> volunteerOpt = volunteerRepository.findByEmail(email);
//        return volunteerOpt.map(v -> {
//            VolunteerDTO dto = new VolunteerDTO();
//            dto.setName(v.getName());
//            dto.setEmail(v.getEmail());
//            dto.setPhone(v.getPhone());
//            dto.setSkills(v.getSkills());
//            dto.setActive(v.getActive());
//            return dto;
//        });
//    }

    // Service method to get volunteer by email (from JWT)
    public Volunteer getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Volunteer not found with email: " + email));
    }

}
