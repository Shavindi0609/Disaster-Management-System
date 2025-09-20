package com.ijse.gdse.back_end.service;

public interface OTPService {

    String generateOtp(String email);

    boolean verifyOtp(String email, String otp);

    void clearOtp(String email);
}
