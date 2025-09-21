package com.veprojects.qdoc.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    private final Random random = new Random();

    public String generateOtp(String phone) {
        String otp=String.format("%06d", random.nextInt(999999));
        System.out.println("DEBUG OTP for " + phone + " = " + otp);
        return otp;
    }

    public void sendOtp(String phone, String otp) {
        System.out.println("DEBUG OTP for " + phone + " = " + otp);
    }
}
