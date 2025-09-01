package com.nghia.ecommerce.controller;

import com.nghia.ecommerce.dto.request.CommentCreateRequest;
import com.nghia.ecommerce.dto.response.CommentResponse;
import com.nghia.ecommerce.security.UserPrincipal;
import com.nghia.ecommerce.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capsules/{capsuleId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponse addComment(
            @PathVariable Long capsuleId,
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CommentCreateRequest request) {
        return commentService.addComment(capsuleId, user.getId(), request);
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long capsuleId) {
        return commentService.getCommentsByCapsule(capsuleId);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CommentCreateRequest request) {
        return commentService.updateComment(commentId, user.getId(), request.getContent());
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal user) {
        commentService.deleteComment(commentId, user.getId());
    }
}
