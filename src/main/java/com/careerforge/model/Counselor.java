package com.careerforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "counselors")
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String specialization;

    @Column(length = 200)
    private String domain;

    @Column(length = 100)
    private String exp;

    private Double rating = 5.00;

    private Integer sessions = 0;

    @Column(length = 10)
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String slots;

    @Column(length = 50)
    private String badge = "badge-teal";

    @Column(length = 500)
    private String resume = "#";

    @Column(length = 50)
    private String price = "Free";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getExp() { return exp; }
    public void setExp(String exp) { this.exp = exp; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getSessions() { return sessions; }
    public void setSessions(Integer sessions) { this.sessions = sessions; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSlots() { return slots; }
    public void setSlots(String slots) { this.slots = slots; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
