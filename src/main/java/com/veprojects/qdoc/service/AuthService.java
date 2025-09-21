package com.veprojects.qdoc.service;

import com.veprojects.qdoc.entities.Role;
import com.veprojects.qdoc.security.AuthException;
import com.veprojects.qdoc.dtos.AuthResponse;
import com.veprojects.qdoc.entities.RefreshToken;
import com.veprojects.qdoc.entities.User;
import com.veprojects.qdoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private OtpService otpService;

    private static final int MAX_RETRIES=5;
    private static final int OTP_EXPIRY_IN_MINUTES=3;

    public void signup(String phone) throws AuthException {
        User user = userRepo.findByPhoneNumber(phone).orElse(null);

        if(user!=null){
            if (user.isVerified()) {
                throw new AuthException("User already exist");
            }
        }else {
            user = new User();
            user.setPhoneNumber(phone);
            user.setRole(Role.PATIENT);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
        }

        user.setOtp(otpService.generateOtp(user.getPhoneNumber()));
        user.setOtpAttempts(0);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_IN_MINUTES));
        userRepo.save(user);
    }

    public void loginWithOTP(String phone) throws AuthException {
        User user = userRepo.findByPhoneNumber(phone).orElse(null);
        if(user==null || !user.isVerified()){
            throw new AuthException("User not exist.Please sign up!");
        }
        user.setOtp(otpService.generateOtp(user.getPhoneNumber()));
        user.setOtpAttempts(0);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_IN_MINUTES));
        userRepo.save(user);
    }

    @Transactional
    public AuthResponse verifyOtp(String phone,String otp) throws AuthException {
        User user = userRepo.findByPhoneNumber(phone).orElse(null);

        if(user==null){
            throw new AuthException("Phone Number not exist!");
        }else if(user.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthException("OTP expired");
        }else if(user.getOtpAttempts()>=MAX_RETRIES){
            throw new AuthException(("Max retries exceeded. Request new OTP."));
        }else if(!user.getOtp().equals(otp)){
            user.setOtpAttempts(user.getOtpAttempts()+1);
            userRepo.save(user);
            throw new AuthException("Invalid OTP");
        }else {
            user.setVerified(true);
            userRepo.save(user);
            String accessToken = jwtService.generateAccessToken(user);
            RefreshToken refreshToken = refreshTokenService.createToken(user);

            return new AuthResponse(accessToken, refreshToken.getToken());
        }
    }

    public AuthResponse login(String phone, String password) throws AuthException {
        User user = userRepo.findByPhoneNumber(phone)
                .orElseThrow(() -> new AuthException("User not found"));

        if (!password.equals(user.getPasswordHash())) { // use encoder in real code
            throw new AuthException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refresh(String refreshTokenValue) throws AuthException {

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshTokenValue);

        if (refreshToken==null){
            throw  new AuthException("Invalid refresh token");
        }

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser());
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public void logout(String refreshTokenValue){
        refreshTokenService.revoke(refreshTokenValue);
    }
}
