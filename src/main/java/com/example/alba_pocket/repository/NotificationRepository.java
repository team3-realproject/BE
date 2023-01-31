package com.example.alba_pocket.repository;

import com.example.alba_pocket.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("select n from Notification n where n.receiver.id = :userId order by n.id desc")
    List<Notification> findAllByUserId(@Param("userId") Long userId);

    @Query("select count(n) from Notification n where n.receiver.id = :userId and n.isRead = false")
    Long countUnReadNotifications(@Param("userId") Long userId);

    Optional<Notification> findById(Long NotificationsId);

    void deleteAllByReceiverId(Long receiverId);
    void deleteById(Long notificationId);


}
