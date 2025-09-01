package com.nghia.ecommerce.controller;

import com.nghia.ecommerce.dto.request.CapsuleCreateRequest;
import com.nghia.ecommerce.dto.request.CapsuleUpdateRequest;
import com.nghia.ecommerce.dto.response.CapsuleResponse;
import com.nghia.ecommerce.security.UserPrincipal;
import com.nghia.ecommerce.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capsules")
@RequiredArgsConstructor
public class CapsuleController {

    private final CapsuleService capsuleService;

    @PostMapping
    public CapsuleResponse createCapsule(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CapsuleCreateRequest request) {
        return capsuleService.createCapsule(user.getId(), request);
    }

    @PutMapping("/{id}")
    public CapsuleResponse updateCapsule(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CapsuleUpdateRequest request) {
        return capsuleService.updateCapsule(id, user.getId(), request);
    }

    @DeleteMapping("/{id}")
    public void deleteCapsule(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        capsuleService.deleteCapsule(id, user.getId());
    }

    @GetMapping("/{id}")
    public CapsuleResponse getCapsuleById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        return capsuleService.getCapsuleById(id, user.getId());
    }

    @GetMapping("/me")
    public List<CapsuleResponse> getMyCapsules(@AuthenticationPrincipal UserPrincipal user) {
        return capsuleService.getMyCapsules(user.getId());
    }

    @GetMapping("/public")
    public List<CapsuleResponse> getPublicCapsules() {
        return capsuleService.getPublicCapsules();
    }

    @PostMapping("/{id}/like")
    public void likeCapsule(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        capsuleService.likeCapsule(id, user.getId());
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlikeCapsule(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        capsuleService.unLikeCapsule(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{capsuleId}/unlock")
    public void manualUnlockCapsule(
            @PathVariable Long capsuleId,
            @AuthenticationPrincipal UserPrincipal user) {
        capsuleService.manualUnlockCapsule(capsuleId, user.getId());
    }

}
