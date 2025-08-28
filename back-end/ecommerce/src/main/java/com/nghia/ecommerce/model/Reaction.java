package com.nghia.ecommerce.model;

import com.nghia.ecommerce.model.enums.ReactionTarget;
import com.nghia.ecommerce.model.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "targetType", "targetId", "reactionType"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReactionTarget targetType;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}
