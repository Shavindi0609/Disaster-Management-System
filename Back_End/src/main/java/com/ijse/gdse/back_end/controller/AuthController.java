package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.dto.*;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.service.AuthService;
import com.ijse.gdse.back_end.service.VolunteerService;
import com.ijse.gdse.back_end.service.impl.VolunteerServiceImpl;
import com.ijse.gdse.back_end.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342") // 👈 FRONTEND origin එක
public class AuthController {
    private final AuthService authService;
    private final VolunteerService volunteerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ------------------ USER AUTH ------------------

    @PostMapping("/register-user")
    public ResponseEntity<APIResponse> registerUser(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "User Registered Successfully",
                        authService.register(registerDTO)
                )
        );
    }

    @PostMapping("/login-user")
    public ResponseEntity<APIResponse> loginUser(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "OK",
                        authService.authenticate(authDTO)
                )
        );
    }

    // ------------------ VOLUNTEER AUTH ------------------

    @PostMapping("/register-volunteer")
    public ResponseEntity<?> registerVolunteer(@RequestBody VolunteerDTO dto) {
        String msg = volunteerService.registerVolunteer(dto);
        return ResponseEntity.ok(Map.of("message", msg));
    }

    @PostMapping("/login-volunteer")
    public ResponseEntity<?> loginVolunteer(@RequestBody VolunteerDTO dto) {
        try {
            Volunteer volunteer = volunteerService.authenticateVolunteer(
                    dto.getEmail(),
                    dto.getPassword()
            );

            String token = jwtUtil.generateToken(volunteer.getEmail());

            VolunteerResponseDTO response = new VolunteerResponseDTO(
                    token,
                    volunteer.getName(),
                    "VOLUNTEER",
                    volunteer.getEmail()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }

}
