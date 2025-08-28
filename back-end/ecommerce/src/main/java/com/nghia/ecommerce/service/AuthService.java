package com.nghia.ecommerce.service;

import com.nghia.ecommerce.dto.request.AuthRequest;
import com.nghia.ecommerce.dto.request.SignupRequest;
import com.nghia.ecommerce.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse signup(SignupRequest request);
    AuthResponse signin(AuthRequest request);
    AuthResponse refreshToken(String refreshToken);
    void changePassword(String username, String oldPassword, String newPassword);


}
