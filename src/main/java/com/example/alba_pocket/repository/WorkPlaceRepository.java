package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.WorkPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkPlaceRepository extends JpaRepository<WorkPlace, Long> {
    List<WorkPlace> findAllByUserId(Long user_id);
}
