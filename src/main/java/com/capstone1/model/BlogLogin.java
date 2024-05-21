package com.capstone1.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BlogLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    private LocalDateTime loginTime;
    private LoginStatus loginStatus;

    public BlogLogin() {
    }

    public BlogLogin(Staff staff, LocalDateTime loginTime, LoginStatus loginStatus) {
        this.staff = staff;
        this.loginTime = loginTime;
        this.loginStatus = loginStatus;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getLoginTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = this.loginTime.format(formatter);

        return formatDateTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public long getId() {
        return id;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public enum LoginStatus {
        LOGIN, LOGOUT
    }
}
