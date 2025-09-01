package com.nghia.ecommerce.service;

import com.nghia.ecommerce.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void sendNotification(Long userId, String message);
    List<NotificationResponse> getMyNotifications(Long userId);
    void markAsRead(Long notificationId, Long userId);
}
