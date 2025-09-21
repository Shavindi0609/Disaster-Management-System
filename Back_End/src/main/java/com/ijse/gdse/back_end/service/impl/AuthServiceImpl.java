package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.dto.AuthDTO;
import com.ijse.gdse.back_end.dto.AuthResponseDTO;
import com.ijse.gdse.back_end.dto.RegisterDTO;
import com.ijse.gdse.back_end.entity.Role;
import com.ijse.gdse.back_end.entity.User;
import com.ijse.gdse.back_end.repository.UserRepository;
import com.ijse.gdse.back_end.service.AuthService;
import com.ijse.gdse.back_end.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO authenticate(AuthDTO authDTO) {

        // 🔹 Validate credentials using email instead of username
        User user = userRepository.findByEmail(authDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        // 🔹 Check password
        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        // 🔹 Generate JWT token (use email as subject)
        String token = jwtUtil.generateToken(user.getEmail());

        // 🔹 Return token, email, role, and name
        return new AuthResponseDTO(
                token,            // accessToken
                user.getUsername(), // username / display name
                user.getRole().name(), // role
                user.getEmail()      // email
        );

    }


//   public String register(RegisterDTO registerDTO) {
//       if (userRepository.findByUsername(registerDTO.getUsername())
//               .isPresent()) {
//           throw new RuntimeException("Username already exists");
//       }
//
//       User user = User.builder()
//               .username(registerDTO.getUsername())
//               .password(passwordEncoder.encode(registerDTO.getPassword()))
//               .role(Role.valueOf(registerDTO.getRole()))
//               .build();
//       userRepository.save(user);
//       return "User registerd successfully";
//   }

    public String register(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // default role USER
        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())  // <-- add this line
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.USER)  // default USER
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

}