package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final JavaMailSender mailSender;

    public void sendEmergencyEmail(String userEmail, String city, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(userEmail);
        mail.setSubject("🚨 EMERGENCY: Weather Alert for " + city);
        mail.setText("Attention!\n\n" + message + "\n\nPlease take necessary precautions immediately.\n\n- NDMRS Team");

        try {
            mailSender.send(mail);
            System.out.println("Emergency alert sent to: " + userEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
