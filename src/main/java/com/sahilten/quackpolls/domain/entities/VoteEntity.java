package com.sahilten.quackpolls.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vote")
public class VoteEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private PollEntity pollEntity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private OptionEntity optionEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity userEntity;

    @Column(name = "voted_at", nullable = false)
    private LocalDateTime votedAt = LocalDateTime.now();

    @Column(name = "voter_identifier", nullable = false)
    private String voterIdentifier;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VoteEntity that = (VoteEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(pollEntity, that.pollEntity) && Objects.equals(
                optionEntity, that.optionEntity) && Objects.equals(userEntity, that.userEntity) && Objects.equals(
                votedAt, that.votedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pollEntity, optionEntity, userEntity, votedAt);
    }

    @PrePersist
    protected void onCreate() {
        this.votedAt = LocalDateTime.now();
    }

    //    @PreUpdate
    //    protected void onUpdate() {
    //        this.updatedAt = LocalDateTime.now();
    //    }
}
