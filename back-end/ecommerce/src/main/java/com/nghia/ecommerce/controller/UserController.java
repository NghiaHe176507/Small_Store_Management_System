package com.nghia.ecommerce.controller;

import com.nghia.ecommerce.dto.request.UpdateProfileRequest;
import com.nghia.ecommerce.dto.response.UserProfileResponse;
import com.nghia.ecommerce.security.UserPrincipal;
import com.nghia.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public UserProfileResponse getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getProfile(principal.getUsername());
    }

    @PutMapping("/profile")
    public UserProfileResponse updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                             @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(principal.getUsername(), request);
    }
}

