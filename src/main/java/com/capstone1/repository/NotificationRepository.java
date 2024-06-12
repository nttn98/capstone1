package com.capstone1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE notifications AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();

    List<Notification> findByUserIdOrderByIdDesc(long userId);

    @Transactional
    void deleteByIdAndUserId(Long id, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId AND n.user.id = :userId")
    void markNotificationAsRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);
}
