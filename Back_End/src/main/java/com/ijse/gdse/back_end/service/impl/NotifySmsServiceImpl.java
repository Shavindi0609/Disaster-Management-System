package com.ijse.gdse.back_end.service.impl;

import com.ijse.gdse.back_end.service.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@Primary
public class NotifySmsServiceImpl implements SmsService {

    @Value("${notify.api.key}")
    private String apiKey;

    @Value("${notify.user.id}")
    private String userId;

    @Value("${notify.sender.id}")
    private String senderId;

    @Override
    public void sendAlert(String to, String city, String risk) {
        try {
            // අංකය 947... ආකෘතියට සකසා ගැනීම
            String formattedPhone = to.replace("+", "");

            // පණිවිඩය සකස් කිරීම
            String message = "NDMS Alert: High risk in " + city + ". Stay safe!";
            // URLEncoder.encode එකේ එන '+' ලකුණ '%20' වලට වෙනස් කිරීම
            String encodedMessage = URLEncoder.encode(message, "UTF-8").replace("+", "%20");

            String urlString = "https://app.notify.lk/api/v1/send?user_id=" + userId +
                    "&api_key=" + apiKey +
                    "&to=" + formattedPhone +
                    "&sender_id=" + senderId +
                    "&message=" + encodedMessage;

            // HTTP Get Request එකක් යැවීම
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                System.out.println("Notify.lk: SMS Sent Successfully to " + to);
            } else {
                // Notify.lk එකෙන් දෙන error message එක කියවීම
                java.util.Scanner s = new java.util.Scanner(conn.getErrorStream()).useDelimiter("\\A");
                String response = s.hasNext() ? s.next() : "";
                System.err.println("Notify.lk Error Response: " + response);
            }

        } catch (Exception e) {
            System.err.println("Error with Notify.lk: " + e.getMessage());
        }
    }
}
