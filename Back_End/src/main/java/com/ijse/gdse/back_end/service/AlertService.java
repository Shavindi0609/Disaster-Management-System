package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.AlertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

public interface AlertService {
    void sendEmergencyEmail(String userEmail, String city, String message);
    String saveSubscriber(AlertDTO alertDTO);
}
