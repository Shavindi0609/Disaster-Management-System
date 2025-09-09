package com.ijse.gdse.back_end.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OTPService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6 digit
        otpStorage.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(5));
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;
        if (otpExpiry.get(email).isBefore(LocalDateTime.now())) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }
        return otpStorage.get(email).equals(otp);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
        otpExpiry.remove(email);
    }
}
