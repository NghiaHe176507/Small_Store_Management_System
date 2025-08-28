package com.nghia.ecommerce.repository;

import com.nghia.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    default Optional<User> findByUsernameOrEmail(String login) {
        if (login == null) return Optional.empty();
        return findByUsername(login).or(() -> findByEmail(login));
    }
}
