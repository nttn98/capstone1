package com.capstone1.model;

public class GooglePojo {
    private String id;
    private String accountName;
    private String email;

    public GooglePojo() {
    }

    public GooglePojo(String id, String name, String email) {
        this.id = id;
        this.accountName = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String name) {
        this.accountName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "GooglePojo{" +
                "id='" + id + '\'' +
                ", accountName='" + accountName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
