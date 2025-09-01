package com.nghia.ecommerce.controller;

import com.nghia.ecommerce.dto.response.NotificationResponse;
import com.nghia.ecommerce.security.UserPrincipal;
import com.nghia.ecommerce.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getMyNotifications(@AuthenticationPrincipal UserPrincipal user) {
        return notificationService.getMyNotifications(user.getId());
    }

    @PutMapping("/{notificationId}/read")
    public void markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserPrincipal user) {
        notificationService.markAsRead(notificationId, user.getId());
    }
}
