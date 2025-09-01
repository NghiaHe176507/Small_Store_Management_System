package com.nghia.ecommerce.service;

import com.nghia.ecommerce.dto.request.CommentCreateRequest;
import com.nghia.ecommerce.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(Long capsuleId, Long authorId, CommentCreateRequest request);
    CommentResponse updateComment(Long commentId, Long userId, String content);
    void deleteComment(Long commentId, Long userId);
    List<CommentResponse> getCommentsByCapsule(Long capsuleId);
}
