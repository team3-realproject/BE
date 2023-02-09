package com.example.alba_pocket.repository;

import com.example.alba_pocket.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByUserIdAndWorkDay(Long user_id, LocalDate workDay);

    List<Calendar> findAllByWorkDayAndAndWorkId(LocalDate localDate, Long workplaceId);

    List<Calendar> findAllByUserIdAndAndWorkId(Long userId, Long workplaceId);

    List<Calendar> findAllByWorkIdAndWorkDayBetween(Long work_id, LocalDate startDay, LocalDate endDay);

    List<Calendar> findAllByUserIdAndWorkDayBetween(Long user_id, LocalDate startDay, LocalDate endDay);

    List<Calendar> findAllByUserIdAndWorkIdAndWorkDayBetween(Long user_id, Long work_id, LocalDate startDay, LocalDate endDay);

}
