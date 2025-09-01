package com.nghia.ecommerce.service.impl;

import com.nghia.ecommerce.dto.request.CapsuleCreateRequest;
import com.nghia.ecommerce.dto.request.CapsuleUpdateRequest;
import com.nghia.ecommerce.dto.response.CapsuleResponse;
import com.nghia.ecommerce.model.Capsule;
import com.nghia.ecommerce.model.Reaction;
import com.nghia.ecommerce.model.User;
import com.nghia.ecommerce.model.enums.CapsuleStatus;
import com.nghia.ecommerce.model.enums.ReactionType;
import com.nghia.ecommerce.repository.CapsuleRepository;
import com.nghia.ecommerce.repository.ReactionRepository;
import com.nghia.ecommerce.repository.UserRepository;
import com.nghia.ecommerce.service.CapsuleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {

    private final CapsuleRepository capsuleRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;
    private final NotificationServiceImpl notificationService;

    @Override
    @Transactional
    public CapsuleResponse createCapsule(Long userId, CapsuleCreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Capsule capsule = Capsule.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .unlockDate(request.getUnlockDate())
                .status(CapsuleStatus.LOCKED)
                .visibility(request.getVisibility())
                .author(author)
                .build();

        capsuleRepository.save(capsule);
        return mapToResponse(capsule);
    }

    @Override
    @Transactional
    public CapsuleResponse updateCapsule(Long capsuleId, Long userId, CapsuleUpdateRequest request) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));

        if (!capsule.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Not your capsule");
        }

        capsule.setTitle(request.getTitle());
        capsule.setContent(request.getContent());
        capsule.setUnlockDate(request.getUnlockDate());
        capsule.setVisibility(request.getVisibility());
        capsule.setUpdatedAt(LocalDateTime.now());

        capsuleRepository.save(capsule);
        return mapToResponse(capsule);
    }

    @Override
    public void deleteCapsule(Long capsuleId, Long userId) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));

        if (!capsule.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Not your capsule");
        }

        capsule.setStatus(CapsuleStatus.DELETED);
        capsuleRepository.save(capsule);
    }

    @Override
    public CapsuleResponse getCapsuleById(Long capsuleId, Long userId) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));

        // Nếu capsule PRIVATE mà user không phải author -> chặn
        if (capsule.getVisibility() == com.nghia.ecommerce.model.enums.Visibility.PRIVATE
                && !capsule.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponse(capsule);
    }

    @Override
    public List<CapsuleResponse> getMyCapsules(Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return capsuleRepository.findByAuthor(author).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapsuleResponse> getPublicCapsules() {
        return capsuleRepository.findByVisibilityAndStatus(
                        com.nghia.ecommerce.model.enums.Visibility.PUBLIC,
                        CapsuleStatus.UNLOCKED
                ).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void likeCapsule(Long capsuleId, Long userId) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!capsule.getAuthor().getId().equals(userId)) {
            notificationService.sendNotification(
                    capsule.getAuthor().getId(),
                    user.getUsername() + " đã thích capsule của bạn."
            );
        }

        reactionRepository.findByUserAndCapsule(user, capsule)
                .ifPresentOrElse(
                        reactionRepository::delete, // nếu đã like thì bỏ like
                        () -> {
                            Reaction r = Reaction.builder()
                                    .type(ReactionType.LIKE)
                                    .capsule(capsule)
                                    .user(user)
                                    .build();
                            reactionRepository.save(r);
                        }
                );

    }

    @Override
    public void unLikeCapsule(Long capsuleId, Long userId) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        reactionRepository.findByUserAndCapsule(user, capsule)
                .ifPresent(reactionRepository::delete);
    }

    private CapsuleResponse mapToResponse(Capsule capsule) {
        long likeCount = reactionRepository.countByCapsule(capsule);
        return CapsuleResponse.builder()
                .id(capsule.getId())
                .title(capsule.getTitle())
                .content(capsule.getContent())
                .author(capsule.getAuthor().getUsername())
                .createdAt(capsule.getCreatedAt())
                .updatedAt(capsule.getUpdatedAt())
                .unlockDate(capsule.getUnlockDate())
                .status(capsule.getStatus())
                .visibility(capsule.getVisibility())
                .likeCount(likeCount)
                .build();
    }

    @Override
    public void manualUnlockCapsule(Long capsuleId, Long userId) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));

        // Chỉ cho phép admin hoặc chính tác giả mở
//        if (!capsule.getAuthor().getId().equals(userId)) {
//            throw new AccessDeniedException("Bạn không có quyền mở capsule này");
//        }

        if (capsule.getStatus() == CapsuleStatus.UNLOCKED) {
            throw new IllegalStateException("Capsule đã được mở trước đó");
        }

        capsule.setStatus(CapsuleStatus.UNLOCKED);
        capsuleRepository.save(capsule);

        notificationService.sendNotification(
                capsule.getAuthor().getId(),
                "Capsule \"" + capsule.getTitle() + "\" của bạn đã được mở khóa thủ công!"
        );
    }

}
