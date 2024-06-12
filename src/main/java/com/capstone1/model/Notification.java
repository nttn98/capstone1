package com.capstone1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String message;
    private boolean isRead;

    private LocalDateTime notiDate;

    public Notification() {
    }

    public Notification(User user, String message, boolean isRead, LocalDateTime notiDate) {
        this.user = user;
        this.message = message;
        this.isRead = isRead;
        this.notiDate = notiDate;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getNotiDate() {
        return notiDate;
    }

    public void setNotiDate(LocalDateTime notiDate) {
        this.notiDate = notiDate;
    }

}
