package com.capstone1.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.Notification;
import com.capstone1.repository.NotificationRepository;
import com.capstone1.services.NotificationService;

@Service
public class Notificationimpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public Notification save(Notification notification) {
        notificationRepository.alterAutoIncrementValue();
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findByUserIdOrderByIdDesc(long userId) {
        return notificationRepository.findByUserIdOrderByIdDesc(userId);

    }

    @Override
    public void deleteByIdAndUserId(long id, long userId) {
        notificationRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public void markNotificationAsRead(long notificationId, long userId) {
        notificationRepository.markNotificationAsRead(notificationId, userId);
    }
}
