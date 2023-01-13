package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findByUser(User user);

    List<Work> findAllByUserId(Long user_id);

    boolean existsById(Long id);
}
