package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendVolunteerAssignmentEmail(String toEmail, String reportType, String reportDescription) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Disaster Report Assigned");
        message.setText("Hello,\n\nYou have been assigned to a new disaster report.\n\n"
                + "Report Type: " + reportType + "\n"
                + "Description: " + reportDescription + "\n\n"
                + "Please take necessary action.\n\nThanks.");
        mailSender.send(message);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP is: " + otp + "\n\nThis code will expire in 5 minutes.");
        mailSender.send(message);
    }

}
