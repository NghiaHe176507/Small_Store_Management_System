package com.nghia.ecommerce.service;

import com.nghia.ecommerce.dto.request.CapsuleCreateRequest;
import com.nghia.ecommerce.dto.request.CapsuleUpdateRequest;
import com.nghia.ecommerce.dto.response.CapsuleResponse;

import java.util.List;

public interface CapsuleService {
    CapsuleResponse createCapsule(Long userId, CapsuleCreateRequest request);
    CapsuleResponse updateCapsule(Long capsuleId, Long userId, CapsuleUpdateRequest request);
    void deleteCapsule(Long capsuleId, Long userId);
    CapsuleResponse getCapsuleById(Long capsuleId, Long userId);
    List<CapsuleResponse> getMyCapsules(Long userId);
    List<CapsuleResponse> getPublicCapsules();
    void likeCapsule(Long capsuleId, Long userId);
    void unLikeCapsule(Long capsuleId, Long userId);
    void manualUnlockCapsule(Long capsuleId, Long userId); //test capsule unlock
}
