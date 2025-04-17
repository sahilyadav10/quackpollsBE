package com.sahilten.quackpolls.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "polls")
public class PollEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String question;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "closes_at")
    private LocalDateTime closesAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionEntity> options = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PollEntity that = (PollEntity) o;
        return isPublic == that.isPublic && Objects.equals(id, that.id) && Objects.equals(question,
                that.question) && Objects.equals(closesAt, that.closesAt) && Objects.equals(createdAt,
                that.createdAt) && Objects.equals(user, that.user) && Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, isPublic, closesAt, createdAt, user, options);
    }

    //   The @PrePersist method runs only when Hibernate executes save/persist.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
