package com.nghia.ecommerce.service.impl;

import com.nghia.ecommerce.dto.request.AuthRequest;
import com.nghia.ecommerce.dto.request.ChangePasswordRequest;
import com.nghia.ecommerce.dto.request.RefreshTokenRequest;
import com.nghia.ecommerce.dto.request.SignupRequest;
import com.nghia.ecommerce.dto.response.AuthResponse;
import com.nghia.ecommerce.model.User;
import com.nghia.ecommerce.model.enums.Role;
import com.nghia.ecommerce.repository.UserRepository;
import com.nghia.ecommerce.security.JwtUtil;
import com.nghia.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword()) // không encode
                .roles(Set.of(Role.USER)) // mặc định USER
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), Set.of("USER"));
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse signin(AuthRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(),
                user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()));
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(),
                user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()));
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Old password incorrect");
        }

        user.setPassword(newPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

}
