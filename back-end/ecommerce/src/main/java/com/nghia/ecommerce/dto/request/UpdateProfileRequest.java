package com.nghia.ecommerce.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    private String email;
    private String avatarUrl;
    private String bio;
    private LocalDate dob;
}
