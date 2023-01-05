package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByUserId(Long userId);
}
