package com.capstone1.model;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String userUsername;
    private String userPassword;
    private long userNumberphone;
    private String userAddress;
    private String userFullname;
    private String userEmail;
    private Date useDob;
    private int userStatus;

    public User() {
        super();
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserNumberphone(long userNumberphone) {
        this.userNumberphone = userNumberphone;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUseDob(Date useDob) {
        this.useDob = useDob;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public long getUserNumberphone() {
        return userNumberphone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Date getUseDob() {
        return useDob;
    }

    public int getUserStatus() {
        return userStatus;
    }

}
