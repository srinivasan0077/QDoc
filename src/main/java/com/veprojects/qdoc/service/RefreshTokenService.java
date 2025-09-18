package com.veprojects.qdoc.service;

import com.veprojects.qdoc.entities.RefreshToken;
import com.veprojects.qdoc.entities.User;
import com.veprojects.qdoc.repository.RefreshTokenRepository;
import com.veprojects.qdoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
public class RefreshTokenService {

    private static final int MAX_TOKENS_PER_USER = 5;

    @Autowired
    private RefreshTokenRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    public RefreshToken createToken(User user) {

        // generate new JWT refresh token
        String token = jwtService.generateRefreshToken(user);

        List<RefreshToken> tokens = repo.findByUser(user);

        // revoke oldest if over limit
        if (tokens.size() >= MAX_TOKENS_PER_USER) {
            // remove oldest (assumes createdAt is set)
            tokens.stream().min(Comparator.comparing(RefreshToken::getCreatedAt))
                    .ifPresent(repo::delete);
        }

        // save refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(LocalDateTime.now());
        repo.save(refreshToken);

        return refreshToken;

    }

    public RefreshToken validateRefreshToken(String token) {
        try {
            Long userId = jwtService.extractUserId(token); // parse JWT payload
            List<RefreshToken> tokens = repo.findByUserId(userId);

            return tokens.stream().filter(rt -> rt.getToken().equals(token)) // find the matching token
                    .findFirst()                             // Optional<RefreshToken>
                    .orElse(null);
        } catch (Exception e) {
            //Log Exception
            return null;
        }
    }

    public void revoke(String token) {
        try {
            Long userId = jwtService.extractUserId(token);
            List<RefreshToken> tokens = repo.findByUserId(userId);

            tokens.stream()
                    .filter(rt -> rt.getToken().equals(token))
                    .findFirst()
                    .ifPresent(repo::delete);

        } catch (Exception e) {
            // invalid token, nothing to revoke
        }
    }
}
