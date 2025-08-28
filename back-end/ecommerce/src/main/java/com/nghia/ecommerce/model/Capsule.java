package com.nghia.ecommerce.model;

import com.nghia.ecommerce.model.enums.CapsuleStatus;
import com.nghia.ecommerce.model.enums.Visibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "capsules", indexes = {
        @Index(name = "idx_unlock_status", columnList = "unlockAt, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Capsule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String coverImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id")
    private User author;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipient_id")
    private User recipient;

    @Enumerated(EnumType.STRING)
    private CapsuleStatus status;

    private LocalDateTime unlockAt;

    private Integer views = 0;
    private Integer commentsCount = 0;
    private Integer likes = 0;

    private Instant createdAt;
    private Instant updatedAt;

    @ElementCollection
    @CollectionTable(name="capsules_tags", joinColumns=@JoinColumn(name="capsule_id"))
    @Column(name="tag")
    private List<String> tags;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = createdAt;
        if (status == null) status = CapsuleStatus.LOCKED;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
