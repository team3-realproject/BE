package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCheckRepository extends JpaRepository<EmailCheck, Long> {

    Optional<EmailCheck> findByEmail(String email);
}
