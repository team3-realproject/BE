package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.StatisticsResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findByUser(User user);

    List<Work> findAllByUserId(Long user_id);

    boolean existsById(Long id);
//    @Query(value = "select w.id as placeId, w.place_name as placeName, w.place_color as placeColor, sec_to_time(sum(time_to_sec(c.working_time))) as monthWorkTime " +
//            "from work w " +
//            "join calendar c on w.id=c.work_id " +
//            "where w.user_id=:user_id and date_format(c.work_day, '%Y-%m')=date_format('20230101', '%Y-%m') group by w.id", nativeQuery = true)
//    List<StatisticsResponseDto> statisticsTime(@Param("user_id") Long user_id);
}
