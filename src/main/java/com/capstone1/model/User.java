package com.capstone1.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private long numberphone;
    private String address;
    private String fullname;
    private String email;
    private Date dob;
    private int status;
    private String ggID;
    private LoginType type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    List<TokenUser> tokenUsers;

    public User() {
        this.notifications = new ArrayList<>();
        this.tokenUsers = new ArrayList<>();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<Notification> getUnReadNotifications() {
        return this.notifications.stream().filter(n -> !n.isRead()).collect(Collectors.toList());
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(long numberphone) {
        this.numberphone = numberphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGgID() {
        return ggID;
    }

    public void setGgID(String ggID) {
        this.ggID = ggID;
    }

    public enum LoginType {
        LOCAL, EMAIL
    }

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
        this.type = type;
    }

    public String getFirstName() {
        if (this.fullname != null && !this.fullname.isEmpty()) {
            String[] parts = this.fullname.split("\\s+");
            return parts[0];
        }
        return null;
    }

    public String getLastName() {
        if (this.fullname != null && !this.fullname.isEmpty()) {
            String[] parts = this.fullname.split("\\s+");
            if (parts.length > 1) {
                return parts[parts.length - 1];
            } else {
                return parts[0];
            }
        }
        return null;
    }

}
