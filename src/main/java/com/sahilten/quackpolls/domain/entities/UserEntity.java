package com.sahilten.quackpolls.domain.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter // Generates getters, setters
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a no-argument constructor
@Builder // Enables builder pattern for this class
@Entity // Marks this class as a JPA entity
@Table(name = "users") // Maps this entity to the 'users' table in the database
public class UserEntity {

    @Id // Marks this field as the primary key
    @GeneratedValue // Automatically generates a UUID value for the ID
    private UUID id;

    @Column(name = "first_name", nullable = false) // Maps to 'first_name' column, not nullable
    private String firstName;

    @Column(name = "last_name", nullable = false) // Maps to 'last_name' column, not nullable
    private String lastName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(nullable = false, unique = true) // Must be unique and not null
    private String email;

    @Column(nullable = false) // Cannot be null
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // One user can have many polls
    // 'user' in PollEntity owns the relationship
    // Cascade all operations and remove orphans automatically,
    private List<PollEntity> polls;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName,
                that.lastName) && Objects.equals(email, that.email) && Objects.equals(password,
                that.password) && Objects.equals(polls, that.polls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, polls);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
