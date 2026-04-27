package com.careerforge.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;   // who receives notification
    private String message;

private boolean isRead;

    private String type; // success, warning, info

    private LocalDateTime createdAt;

    // ✅ Default Constructor
    public Notification() {}

    // ✅ All Args Constructor
    public Notification(Long id, String userEmail, String message,
                        boolean isread, String type, LocalDateTime createdAt) {
        this.id = id;
        this.userEmail = userEmail;
        this.message = message;
        this.isRead = isread;
        this.type = type;
        this.createdAt = createdAt;
    }

    // ✅ Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ✅ Auto set time before saving
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}