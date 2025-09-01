package com.nghia.ecommerce.service.impl;

import com.nghia.ecommerce.dto.request.CommentCreateRequest;
import com.nghia.ecommerce.dto.response.CommentResponse;
import com.nghia.ecommerce.model.Capsule;
import com.nghia.ecommerce.model.Comment;
import com.nghia.ecommerce.model.User;
import com.nghia.ecommerce.repository.CapsuleRepository;
import com.nghia.ecommerce.repository.CommentRepository;
import com.nghia.ecommerce.repository.UserRepository;
import com.nghia.ecommerce.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CapsuleRepository capsuleRepo;
    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final NotificationServiceImpl notificationService;

    @Override
    public CommentResponse addComment(Long capsuleId, Long authorId, CommentCreateRequest request) {
        Capsule capsule = capsuleRepo.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));
        User user = userRepo.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .capsule(capsule)
                .author(user)
                .build();

        commentRepo.save(comment);

        // 🔔 Gửi notification cho tác giả capsule
        if (!capsule.getAuthor().getId().equals(authorId)) {
            notificationService.sendNotification(
                    capsule.getAuthor().getId(),
                    user.getUsername() + " đã bình luận vào capsule của bạn."
            );
        }

        return mapToResponse(comment);
    }

    @Override
    public CommentResponse updateComment(Long commentId, Long userId, String content) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("You cannot edit this comment");
        }

        comment.setContent(content);
        commentRepo.save(comment);
        return mapToResponse(comment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("You cannot delete this comment");
        }

        commentRepo.delete(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByCapsule(Long capsuleId) {
        Capsule capsule = capsuleRepo.findById(capsuleId)
                .orElseThrow(() -> new EntityNotFoundException("Capsule not found"));

        return commentRepo.findByCapsuleOrderByCreatedAtAsc(capsule)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
