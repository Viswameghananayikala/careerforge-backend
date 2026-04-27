package com.careerforge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "counselors")
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ IMPORTANT: link with User table (login)
    @Column(nullable = false)
    private Long userId;

    private String name;
    private String title;
    private String specialization;
    private String domain;
    private String exp;
    private Double rating;
    private Integer sessions;
    private String avatar;

    @Column(length = 1000)
    private String bio;

    private String slots;
    private String badge;
    private String resume;
    private String price;

    public Counselor() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId; // ✅ ADD
        private String name;
        private String title;
        private String specialization;
        private String domain;
        private String exp;
        private Double rating;
        private Integer sessions;
        private String avatar;
        private String bio;
        private String slots;
        private String badge;
        private String resume;
        private String price;

        public Builder id(Long id) { this.id = id; return this; }

        // ✅ ADD THIS
        public Builder userId(Long userId) { 
            this.userId = userId; 
            return this; 
        }

        public Builder name(String v) { this.name = v; return this; }
        public Builder title(String v) { this.title = v; return this; }
        public Builder specialization(String v) { this.specialization = v; return this; }
        public Builder domain(String v) { this.domain = v; return this; }
        public Builder exp(String v) { this.exp = v; return this; }
        public Builder rating(Double v) { this.rating = v; return this; }
        public Builder sessions(Integer v) { this.sessions = v; return this; }
        public Builder avatar(String v) { this.avatar = v; return this; }
        public Builder bio(String v) { this.bio = v; return this; }
        public Builder slots(String v) { this.slots = v; return this; }
        public Builder badge(String v) { this.badge = v; return this; }
        public Builder resume(String v) { this.resume = v; return this; }
        public Builder price(String v) { this.price = v; return this; }

        public Counselor build() {
            Counselor c = new Counselor();
            c.id = id;
            c.userId = userId; // ✅ IMPORTANT
            c.name = name;
            c.title = title;
            c.specialization = specialization;
            c.domain = domain;
            c.exp = exp;
            c.rating = rating;
            c.sessions = sessions;
            c.avatar = avatar;
            c.bio = bio;
            c.slots = slots;
            c.badge = badge;
            c.resume = resume;
            c.price = price;
            return c;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // ✅ ADD GETTER/SETTER
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String v) { this.specialization = v; }

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
}