package com.nghia.ecommerce.repository;

import com.nghia.ecommerce.model.Reaction;
import com.nghia.ecommerce.model.User;
import com.nghia.ecommerce.model.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndCapsule(User user, Capsule capsule);
    long countByCapsule(Capsule capsule);
}
