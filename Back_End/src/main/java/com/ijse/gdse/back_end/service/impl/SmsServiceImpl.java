package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.service.SmsService;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhone;
    // Twilio Dashboard එකෙන් ලැබුණු අගයන් මෙලෙස String එකක් ලෙස ලබා දෙන්න
//    public static final String ACCOUNT_SID = "ACe5beefe7909e67f7625cba0b746d9562";
//    public static final String AUTH_TOKEN = "1837393f238af3173fdeaeb61df3ff99";
//    public static final String TWILIO_PHONE = "+16627057600"; // ඔබේ Twilio Number එක මෙතනට

//    public void sendAlert(String to, String city, String risk) {
//        try {
//            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//            Message message = Message.creator(
//                    new PhoneNumber(to),
//                    new PhoneNumber(TWILIO_PHONE),
//                    "⚠️ NDMS ALERT: High " + risk + " risk detected in " + city + ". Please stay safe!"
//            ).create();
//
//            System.out.println("SMS Sent Successfully! SID: " + message.getSid());
//        } catch (Exception e) {
//            System.err.println("Error sending SMS: " + e.getMessage());
//        }
//    }

    @Override // මෙය එක් කිරීමෙන් interface එකේ method එකක් බව තහවුරු වේ
    public void sendAlert(String to, String city, String risk) {
        try {
            Twilio.init(accountSid,authToken);

            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(twilioPhone),
//                    "⚠️ NDMS ALERT: High " + risk + " risk detected in " + city + ". Please stay safe!"
                    "NDMS: High risk in " + city + ". Stay safe!" // පණිවිඩය කෙටි කරන්න
            ).create();

            System.out.println("SMS Sent Successfully! SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
}
