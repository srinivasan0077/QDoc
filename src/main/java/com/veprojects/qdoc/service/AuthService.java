package com.veprojects.qdoc.service;

import com.veprojects.qdoc.dtos.AuthResponse;
import com.veprojects.qdoc.entities.RefreshToken;
import com.veprojects.qdoc.entities.User;
import com.veprojects.qdoc.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepo, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse login(String phone, String password) {
        User user = userRepo.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!password.equals(user.getPasswordHash())) { // use encoder in real code
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refresh(String refreshTokenValue) {

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshTokenValue);

        if (refreshToken==null){
            throw  new RuntimeException("Invalid refresh token");
        }

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser());
        return new AuthResponse(accessToken, refreshToken.getToken());
    }
}
