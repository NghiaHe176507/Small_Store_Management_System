package com.nghia.ecommerce.dto.request;

import com.nghia.ecommerce.model.enums.Visibility;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CapsuleCreateRequest {
    private String title;
    private String content;
    private LocalDateTime unlockDate;
    private Visibility visibility;
}
