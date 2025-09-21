package com.veprojects.qdoc.controller;

import com.veprojects.qdoc.dtos.*;
import com.veprojects.qdoc.security.AuthException;
import com.veprojects.qdoc.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody AuthRequest request) throws AuthException {
        AuthResponse authResponse = authService.login(request.phone(), passwordEncoder.encode(request.password()));
        return ResponseEntity.ok(new Response("Success",authResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Response> refresh(@Valid @RequestBody RefreshRequest request) throws AuthException {
        AuthResponse authResponse=authService.refresh(request.refreshToken());
        return ResponseEntity.ok(new Response("Success",authResponse));
    }


    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@Valid @RequestBody SignUpRequest request) throws AuthException {
        authService.signup(request.phone());
        return ResponseEntity.ok(new Response("Success",null));
    }

    @PostMapping("/login-otp")
    public ResponseEntity<Response> loginWithOTP(@Valid @RequestBody SignUpRequest request) throws AuthException {
        authService.loginWithOTP(request.phone());
        return ResponseEntity.ok(new Response("Success",null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Response> verifyOtp(@Valid @RequestBody OtpRequest request) throws AuthException {
        AuthResponse authResponse=authService.verifyOtp(request.phone(),request.otp());
        return ResponseEntity.ok(new Response("Success",authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@Valid @RequestBody RefreshRequest request){
        authService.logout(request.refreshToken());
        return ResponseEntity.ok(new Response("Success",null));
    }

}
