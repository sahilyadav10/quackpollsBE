package com.sahilten.quackpolls.repositories;


import com.sahilten.quackpolls.domain.entities.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<PollEntity, UUID> {

}