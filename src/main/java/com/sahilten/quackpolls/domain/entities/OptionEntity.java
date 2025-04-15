package com.sahilten.quackpolls.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "options")
public class OptionEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private PollEntity poll;
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OptionEntity that = (OptionEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(poll, that.poll);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, poll);
    }
}
