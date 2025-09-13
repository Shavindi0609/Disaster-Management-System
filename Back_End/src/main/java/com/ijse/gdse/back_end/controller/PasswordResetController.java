package com.ijse.gdse.back_end.controller;

import com.ijse.gdse.back_end.repository.UserRepository;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import com.ijse.gdse.back_end.service.EmailService;
import com.ijse.gdse.back_end.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
@CrossOrigin // frontend URL
public class PasswordResetController {

    private final UserRepository userRepository;
    private final OTPService otpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final VolunteerRepository volunteerRepository; // ✅ add

    // 1. Send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email not found"));
        }

        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent to your email"));
    }

    // 2. Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (otpService.verifyOtp(email, otp)) {
            return ResponseEntity.ok(Map.of("message", "OTP verified"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));
    }

    // 3. Reset Password
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }

        var user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpService.clearOtp(email);

        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    // ================= Volunteer Password Reset =================
    @PostMapping("/volunteers/send-otp")
    public ResponseEntity<?> sendVolunteerOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        var volunteerOpt = volunteerRepository.findByEmail(email);
        if (volunteerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Volunteer email not found"));
        }
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok(Map.of("message", "OTP sent to your email"));
    }

    @PostMapping("/volunteers/verify-otp")
    public ResponseEntity<?> verifyVolunteerOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        if (otpService.verifyOtp(email, otp)) {
            return ResponseEntity.ok(Map.of("message", "OTP verified"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));
    }

    @PostMapping("/volunteers/reset")
    public ResponseEntity<?> resetVolunteerPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        var volunteerOpt = volunteerRepository.findByEmail(email);
        if (volunteerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Volunteer not found"));
        }
        var volunteer = volunteerOpt.get();
        volunteer.setPassword(passwordEncoder.encode(newPassword));
        volunteerRepository.save(volunteer);
        otpService.clearOtp(email);
        return ResponseEntity.ok(Map.of("message", "Volunteer password reset successful"));
    }
}
