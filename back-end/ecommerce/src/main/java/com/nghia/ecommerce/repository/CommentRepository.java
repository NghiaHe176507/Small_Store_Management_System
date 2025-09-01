package com.nghia.ecommerce.repository;

import com.nghia.ecommerce.model.Comment;
import com.nghia.ecommerce.model.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCapsuleOrderByCreatedAtAsc(Capsule capsule);
}
