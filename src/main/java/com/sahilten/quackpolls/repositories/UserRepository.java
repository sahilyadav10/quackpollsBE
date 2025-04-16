package com.sahilten.quackpolls.repositories;

import com.sahilten.quackpolls.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository provides CRUD operations and custom query methods for UserEntity.
 * It extends JpaRepository, which Spring Data JPA auto-implements at runtime.
 * No need for a concrete class — Spring generates the implementation behind the scenes
 * based on method names like findByEmail.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    /**
     * Custom query method to find a user by email.
     * Spring Data JPA automatically implements this method based on its name ("findByEmail").
     * - If a user with the given email exists, returns Optional.of(user)
     * - If no user is found, returns Optional.empty()
     * This allows you to safely handle the case where the user may not exist,
     * avoiding NullPointerExceptions by encouraging use of Optional handling.
     */
    Optional<UserEntity> findByEmail(String email);
}
