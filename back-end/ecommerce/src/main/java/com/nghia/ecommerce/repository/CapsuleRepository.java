package com.nghia.ecommerce.repository;

import com.nghia.ecommerce.model.Capsule;
import com.nghia.ecommerce.model.User;
import com.nghia.ecommerce.model.enums.CapsuleStatus;
import com.nghia.ecommerce.model.enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    List<Capsule> findByVisibilityAndStatus(Visibility visibility, com.nghia.ecommerce.model.enums.CapsuleStatus status);
    List<Capsule> findByAuthor(User author);
    List<Capsule> findByUnlockDateBeforeAndStatus(LocalDateTime now, CapsuleStatus status);

}
