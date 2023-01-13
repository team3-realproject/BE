package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByUserIdAndWorkDay(Long user_id, LocalDate workDay);

    Optional<Calendar> findByWorkDayAndAndWorkPlaceId(LocalDate localDate, Long workplaceId);

    List<Calendar> findAllByUserIdAndAndWorkPlaceId(Long userId, Long workplaceId);

    Optional<Calendar> findByWorkDayAndUserId(LocalDate localDate, Long userId);

    @Query(value = "select * from calendar where user_id = :user_id and work_day between :startDay AND :endDay", nativeQuery = true)
    List<Calendar> findByUserIdAndWorkDay(@Param("user_id") Long user_id, @Param("startDay")LocalDate startDay, @Param("endDay") LocalDate endDay);

//    List<Calendar> findByUserIdAndWorkDayBetween



}
