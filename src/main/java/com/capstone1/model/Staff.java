package com.capstone1.model;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "staffs")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long staffId;
    private String staffFullname;
    private String staffUsername;
    private String staffPassword;
    private long staffNumberphone;
    private String staffEmail;
    private Date staffDob;
    private long staffIdcard;

    private int staffStatus;

    public Staff() {
        super();
    }

    public void setStaffFullname(String staffFullname) {
        this.staffFullname = staffFullname;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    public void setStaffNumberphone(long staffNumberphone) {
        this.staffNumberphone = staffNumberphone;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public void setStaffDob(Date staffDob) {
        this.staffDob = staffDob;
    }

    public void setStaffIdcard(long staffIdcard) {
        this.staffIdcard = staffIdcard;
    }

    public void setStaffStatus(int staffStatus) {
        this.staffStatus = staffStatus;
    }

    public long getStaffId() {
        return staffId;
    }

    public String getStaffFullname() {
        return staffFullname;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public long getStaffNumberphone() {
        return staffNumberphone;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public Date getStaffDob() {
        return staffDob;
    }

    public long getStaffIdcard() {
        return staffIdcard;
    }

    public int getStaffStatus() {
        return staffStatus;
    }

}
