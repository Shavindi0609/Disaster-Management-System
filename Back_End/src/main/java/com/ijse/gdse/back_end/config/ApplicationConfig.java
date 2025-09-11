package com.ijse.gdse.back_end.config;

import com.ijse.gdse.back_end.repository.UserRepository;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            // Try User first
            var userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                var user = userOpt.get();
                return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );
            }

            // Try Volunteer
            var volunteerOpt = volunteerRepository.findByEmail(email);
            if (volunteerOpt.isPresent()) {
                var volunteer = volunteerOpt.get();
                return new org.springframework.security.core.userdetails.User(
                        volunteer.getEmail(),
                        volunteer.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_VOLUNTEER"))
                );
            }

            throw new RuntimeException("User or Volunteer not found with email: " + email);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}