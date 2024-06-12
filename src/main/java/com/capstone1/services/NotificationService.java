package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Notification;

import jakarta.transaction.Transactional;

public interface NotificationService {
    public Notification save(Notification notification);

    public List<Notification> findByUserIdOrderByIdDesc(long userId);

    void markNotificationAsRead(long notificationId, long userId);

    @Transactional
    void deleteByIdAndUserId(long id, long userId);
}
