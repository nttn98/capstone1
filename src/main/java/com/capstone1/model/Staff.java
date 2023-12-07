package com.capstone1.model;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "staffs")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullname;
    private String username;
    private String password;
    private long numberphone;
    private String email;
    private Date dob;
    private long idcard;

    private int status;

    public Staff() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public long getIdcard() {
        return idcard;
    }

    public void setIdcard(long idcard) {
        this.idcard = idcard;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
