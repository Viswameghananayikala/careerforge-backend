package com.careerforge.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)   
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate joinedAt;

    public enum Role { USER, ADMIN, COUNSELOR }
    @Column(nullable = false)
    private boolean blocked = false;
    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) joinedAt = LocalDate.now();
    }

    public User() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String name; private String email;
        private String password; private Role role; private LocalDate joinedAt;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder joinedAt(LocalDate d) { this.joinedAt = d; return this; }
        public User build() {
            User u = new User(); u.id=id; u.name=name; u.email=email;
            u.password=password; u.role=role; u.joinedAt=joinedAt; return u;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public LocalDate getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDate joinedAt) { this.joinedAt = joinedAt; }
    public boolean isBlocked() { return blocked; }
public void setBlocked(boolean blocked) { this.blocked = blocked; }
}
