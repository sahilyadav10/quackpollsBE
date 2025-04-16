package com.sahilten.quackpolls.repositories;

import com.sahilten.quackpolls.domain.entities.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, UUID> {
}
