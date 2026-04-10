package com.careerforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_email", nullable = false, length = 200)
    private String studentEmail;

    @Column(name = "student_name", length = 150)
    private String studentName;

    @Column(name = "counselor_id")
    private Long counselorId;

    @Column(name = "counselor_name", length = 150)
    private String counselorName;

    @Column(name = "session_type", length = 100)
    private String sessionType;

    @Column(length = 50)
    private String date;

    @Column(length = 50)
    private String time;

    @Column(length = 50)
    private String status = "pending";

    @Column(length = 20)
    private String phone;

    @Column(name = "father_name", length = 150)
    private String fatherName;

    @Column(name = "father_phone", length = 20)
    private String fatherPhone;

    @Column(name = "intermediate_grade", length = 50)
    private String intermediateGrade;

    @Column(length = 100)
    private String stream;

    @Column(name = "interested_skills", columnDefinition = "TEXT")
    private String interestedSkills;

    @Column(columnDefinition = "TEXT")
    private String hobbies;

    @Column(name = "interested_branch", length = 200)
    private String interestedBranch;

    @Column(name = "preferred_college", length = 200)
    private String preferredCollege;

    @Column(name = "study_preference", length = 100)
    private String studyPreference;

    @Column(columnDefinition = "TEXT")
    private String goal;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    @PrePersist
    public void prePersist() {
        if (bookedAt == null) bookedAt = LocalDateTime.now();
        if (status == null) status = "pending";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }

    public String getCounselorName() { return counselorName; }
    public void setCounselorName(String counselorName) { this.counselorName = counselorName; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getFatherPhone() { return fatherPhone; }
    public void setFatherPhone(String fatherPhone) { this.fatherPhone = fatherPhone; }

    public String getIntermediateGrade() { return intermediateGrade; }
    public void setIntermediateGrade(String intermediateGrade) { this.intermediateGrade = intermediateGrade; }

    public String getStream() { return stream; }
    public void setStream(String stream) { this.stream = stream; }

    public String getInterestedSkills() { return interestedSkills; }
    public void setInterestedSkills(String interestedSkills) { this.interestedSkills = interestedSkills; }

    public String getHobbies() { return hobbies; }
    public void setHobbies(String hobbies) { this.hobbies = hobbies; }

    public String getInterestedBranch() { return interestedBranch; }
    public void setInterestedBranch(String interestedBranch) { this.interestedBranch = interestedBranch; }

    public String getPreferredCollege() { return preferredCollege; }
    public void setPreferredCollege(String preferredCollege) { this.preferredCollege = preferredCollege; }

    public String getStudyPreference() { return studyPreference; }
    public void setStudyPreference(String studyPreference) { this.studyPreference = studyPreference; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
}
