package com.ijse.gdse.back_end.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

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
}
