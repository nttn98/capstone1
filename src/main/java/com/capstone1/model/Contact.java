package com.capstone1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private long numberphone;

    @Column(columnDefinition = "LONGTEXT")
    private String messages;

    public Contact() {

    }

    public Contact(String name, String email, long numberphone, String messages) {
        this.name = name;
        this.email = email;
        this.numberphone = numberphone;
        this.messages = messages;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(long numberphone) {
        this.numberphone = numberphone;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

}
