package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByUserId(Long userId);

    Optional<Calendar> findByWorkDayAndAndWorkPlaceId(LocalDate localDate, Long workplaceId);

    List<Calendar> findAllByUserIdAndAndWorkPlaceId(Long userId, Long workplaceId);

    Optional<Calendar> findByWorkDayAndUserId(LocalDate localDate, Long userId);



}
