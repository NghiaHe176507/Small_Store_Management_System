package com.nghia.ecommerce.controller;

import com.nghia.ecommerce.dto.request.AuthRequest;
import com.nghia.ecommerce.dto.request.ChangePasswordRequest;
import com.nghia.ecommerce.dto.request.RefreshTokenRequest;
import com.nghia.ecommerce.dto.request.SignupRequest;
import com.nghia.ecommerce.dto.response.AuthResponse;
import com.nghia.ecommerce.security.JwtUtil;
import com.nghia.ecommerce.security.UserPrincipal;
import com.nghia.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/signin")
    public AuthResponse signin(@RequestBody AuthRequest request) {
        return authService.signin(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request.getRefreshToken());
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request,
                                 @AuthenticationPrincipal UserPrincipal userDetails) {
        authService.changePassword(userDetails.getUsername(),
                request.getOldPassword(), request.getNewPassword());
        return "Password changed successfully";
    }

}
