package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.dto.VolunteerDTO;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import com.ijse.gdse.back_end.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {


    private final VolunteerRepository volunteerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
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

    @Override
    public Volunteer authenticateVolunteer(String email, String password) {
        Volunteer volunteer = volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(password, volunteer.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return volunteer;
    }


    @Override
    public Volunteer addVolunteer(VolunteerDTO volunteerDTO) {
        Volunteer volunteer = Volunteer.builder()
                .name(volunteerDTO.getName())
                .email(volunteerDTO.getEmail())
                .phone(volunteerDTO.getPhone())
                .skills(volunteerDTO.getSkills())
                .build();

        return volunteerRepository.save(volunteer);
    }

    @Override
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    @Override
    public Volunteer getVolunteerById(Long id) {
        return volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
    }

    @Override
    public Volunteer updateVolunteer(Long id, VolunteerDTO volunteerDTO) {
        Volunteer volunteer = getVolunteerById(id);

        volunteer.setName(volunteerDTO.getName());
        volunteer.setEmail(volunteerDTO.getEmail());
        volunteer.setPhone(volunteerDTO.getPhone());
        volunteer.setSkills(volunteerDTO.getSkills());

        return volunteerRepository.save(volunteer);
    }

    @Override
    public void deleteVolunteer(Long id) {
        Volunteer volunteer = getVolunteerById(id);
        volunteerRepository.delete(volunteer);
    }

    @Override
    public long countVolunteers() {
        return volunteerRepository.count();
    }

    @Override
    public Volunteer getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Volunteer not found with email: " + email));
    }

}
