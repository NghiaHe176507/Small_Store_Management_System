package com.nghia.ecommerce.dto.response;

import com.nghia.ecommerce.model.enums.CapsuleStatus;
import com.nghia.ecommerce.model.enums.Visibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CapsuleResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime unlockDate;
    private CapsuleStatus status;
    private Visibility visibility;
    private long likeCount;
}
