package com.nghia.ecommerce.model;

import com.nghia.ecommerce.model.enums.CapsuleStatus;
import com.nghia.ecommerce.model.enums.Visibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Capsule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime unlockDate;

    @Enumerated(EnumType.STRING)
    private CapsuleStatus status;   // LOCKED, UNLOCKED, DELETED

    @Enumerated(EnumType.STRING)
    private Visibility visibility; // PUBLIC, PRIVATE, FRIENDS_ONLY

    // Quan hệ với User (author)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    // Reaction (like)
    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactions = new HashSet<>();
}
