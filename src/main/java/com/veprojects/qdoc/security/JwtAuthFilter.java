package com.veprojects.qdoc.security;

import com.veprojects.qdoc.entities.User;
import com.veprojects.qdoc.repository.UserRepository;
import com.veprojects.qdoc.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Long userId= jwtService.extractUserId(token);
                User user = userRepo.findById(userId).orElse(null);
                if (user != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null,
                                    List.of(new SimpleGrantedAuthority(user.getRole().name())));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }catch (JwtException exception){
                // delegate to entry point
                SecurityContextHolder.clearContext();
                jwtAuthEntryPoint.commence(request, response,
                        new InsufficientAuthenticationException("Invalid JWT token", exception));
                return;
            }

        }

        chain.doFilter(request, response);
    }
}

