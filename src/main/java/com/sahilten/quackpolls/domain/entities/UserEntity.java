package com.sahilten.quackpolls.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data // Generates getters, setters, equals, hashCode, toString
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

    @Column(nullable = false, unique = true) // Must be unique and not null
    private String email;

    @Column(nullable = false) // Cannot be null
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // One user can have many polls
    // 'user' in PollEntity owns the relationship
    // Cascade all operations and remove orphans automatically,
    private List<PollEntity> polls;
}