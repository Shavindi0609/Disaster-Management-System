package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.dto.AlertDTO;
import com.ijse.gdse.back_end.entity.AlertSubscriber;
import com.ijse.gdse.back_end.repository.AlertSubscriberRepositor;
import com.ijse.gdse.back_end.service.AlertService;
import com.ijse.gdse.back_end.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final JavaMailSender mailSender;
    @Autowired
    private AlertSubscriberRepositor repository;

    @Autowired
    private SmsService smsService;

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


    @Override
    public String saveSubscriber(AlertDTO alertDTO) {
        // 1. Phone number validation
        if (!alertDTO.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            return "Invalid phone number format!";
        }

        // 2. DTO -> Entity conversion
        AlertSubscriber subscriber = new AlertSubscriber();
        subscriber.setName(alertDTO.getName());
        subscriber.setPhoneNumber(alertDTO.getPhoneNumber());
        subscriber.setCity(alertDTO.getCity());
        subscriber.setActive(true);

        // 3. Database එකට Save කිරීම
        repository.save(subscriber);

        repository.save(subscriber); // 1. Database එකට Save වෙනවා
        smsService.sendAlert(alertDTO.getPhoneNumber(), alertDTO.getCity(), "Registration Successful"); // 2. SMS එක යනවා
        return "Successfully Registered"; // 3. අවසානයට තමයි return වෙන්නේ
        // 4. Save වූ පසු වහාම SMS එකක් යැවීම (Return එකට ඉහළින් තිබිය යුතුයි)
//        try {
//            smsService.sendAlert(alertDTO.getPhoneNumber(), alertDTO.getCity(), "Registration Successful");
//        } catch (Exception e) {
//            System.err.println("SMS sending failed: " + e.getMessage());
//        }
//
//        return "Successfully Registered and SMS Sent to " + alertDTO.getCity();
    }

}
