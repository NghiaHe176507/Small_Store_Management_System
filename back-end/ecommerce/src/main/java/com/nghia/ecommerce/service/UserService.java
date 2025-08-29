package com.nghia.ecommerce.service;

import com.nghia.ecommerce.dto.request.UpdateProfileRequest;
import com.nghia.ecommerce.dto.response.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(String username);
    UserProfileResponse updateProfile(String username, UpdateProfileRequest request);
}
