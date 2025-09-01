package com.nghia.ecommerce.scheduler;

import com.nghia.ecommerce.model.Capsule;
import com.nghia.ecommerce.model.enums.CapsuleStatus;
import com.nghia.ecommerce.repository.CapsuleRepository;
import com.nghia.ecommerce.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapsuleScheduler {

    private final CapsuleRepository capsuleRepo;
    private final NotificationService notificationService;

    // Chạy mỗi ngày lúc 00:00
    @Scheduled(cron = "0 0 0 * * *")
    public void unlockCapsules() {
        log.info("Running Capsule Unlock Job...");

        List<Capsule> capsules = capsuleRepo.findByUnlockDateBeforeAndStatus(
                LocalDateTime.now(), CapsuleStatus.LOCKED);

        for (Capsule capsule : capsules) {
            capsule.setStatus(CapsuleStatus.UNLOCKED);
            capsuleRepo.save(capsule);

            notificationService.sendNotification(
                    capsule.getAuthor().getId(),
                    "Capsule \"" + capsule.getTitle() + "\" của bạn đã được mở khóa!"
            );

            log.info("Unlocked capsule id={} title={}", capsule.getId(), capsule.getTitle());
        }
    }
}
