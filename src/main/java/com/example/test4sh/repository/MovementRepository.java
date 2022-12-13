package com.example.test4sh.repository;

import com.example.test4sh.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long> {
}
