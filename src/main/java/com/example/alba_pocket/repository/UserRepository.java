package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    Optional<User> findById(Long id);

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

}
