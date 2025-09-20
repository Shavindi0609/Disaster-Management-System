package com.ijse.gdse.back_end.service;

public interface EmailService {

    void sendVolunteerAssignmentEmail(String toEmail, String reportType, String reportDescription);

    void sendOtpEmail(String toEmail, String otp);
}
