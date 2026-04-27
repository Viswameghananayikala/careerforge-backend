package com.careerforge.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    
    private String senderEmail;
    private String receiverEmail;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime time;
    private boolean seen = false;
    private boolean delivered = false;
    private String fileUrl;
    private String fileType;


    // Constructors
    public Message() {}

    public Message(String senderEmail, String receiverEmail, String message) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.message = message;
        this.time = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getSenderEmail() { return senderEmail; }
    public String getReceiverEmail() { return receiverEmail; }
    public String getMessage() { return message; }
    public LocalDateTime getTime() { return time; }
    public boolean isSeen() { return seen; }
    public boolean isDelivered() { return delivered; }

    public void setId(Long id) { this.id = id; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }
    public void setMessage(String message) { this.message = message; }
    public void setTime(LocalDateTime time) { this.time = time; }
     public void setSeen(boolean seen) { this.seen = seen; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}