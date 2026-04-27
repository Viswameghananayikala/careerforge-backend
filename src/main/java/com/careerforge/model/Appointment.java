package com.careerforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String studentEmail;
    private Long counselorId;
    private String counselorName;
    private String sessionType;
    private String date;
    private String time;
    private String status;
    private String phone;
    private String fatherName;
    private String fatherPhone;
    private String intermediateGrade;
    private String stream;
    private String interestedSkills;
    private String hobbies;
    private String interestedBranch;
    private String preferredCollege;
    private String studyPreference;
    private String meetingLink;

    @Column(length = 2000)
    private String goal;

    private LocalDateTime bookedAt;

    @PrePersist
    protected void onCreate() {
        if (bookedAt == null) bookedAt = LocalDateTime.now();
        if (status == null) status = "pending";
    }

    public Appointment() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String studentName; private String studentEmail;
        private Long counselorId; private String counselorName; private String sessionType;
        private String date; private String time; private String status;
        private String phone; private String fatherName; private String fatherPhone;
        private String intermediateGrade; private String stream; private String interestedSkills;
        private String hobbies; private String interestedBranch; private String preferredCollege;
        private String studyPreference; private String goal; private LocalDateTime bookedAt;
        private String meetingLink;

        public Builder id(Long v) { this.id=v; return this; }
        public Builder studentName(String v) { this.studentName=v; return this; }
        public Builder studentEmail(String v) { this.studentEmail=v; return this; }
        public Builder counselorId(Long v) { this.counselorId=v; return this; }
        public Builder counselorName(String v) { this.counselorName=v; return this; }
        public Builder sessionType(String v) { this.sessionType=v; return this; }
        public Builder date(String v) { this.date=v; return this; }
        public Builder time(String v) { this.time=v; return this; }
        public Builder status(String v) { this.status=v; return this; }
        public Builder phone(String v) { this.phone=v; return this; }
        public Builder fatherName(String v) { this.fatherName=v; return this; }
        public Builder fatherPhone(String v) { this.fatherPhone=v; return this; }
        public Builder intermediateGrade(String v) { this.intermediateGrade=v; return this; }
        public Builder stream(String v) { this.stream=v; return this; }
        public Builder interestedSkills(String v) { this.interestedSkills=v; return this; }
        public Builder hobbies(String v) { this.hobbies=v; return this; }
        public Builder interestedBranch(String v) { this.interestedBranch=v; return this; }
        public Builder preferredCollege(String v) { this.preferredCollege=v; return this; }
        public Builder studyPreference(String v) { this.studyPreference=v; return this; }
        public Builder goal(String v) { this.goal=v; return this; }
        public Builder bookedAt(LocalDateTime v) { this.bookedAt=v; return this; }
        public Builder meetingLink(String v) { this.meetingLink = v; return this; }

        public Appointment build() {
            Appointment a = new Appointment();
            a.id=id; a.studentName=studentName; a.studentEmail=studentEmail;
            a.counselorId=counselorId; a.counselorName=counselorName;
            a.sessionType=sessionType; a.date=date; a.time=time; a.status=status;
            a.phone=phone; a.fatherName=fatherName; a.fatherPhone=fatherPhone;
            a.intermediateGrade=intermediateGrade; a.stream=stream;
            a.interestedSkills=interestedSkills; a.hobbies=hobbies;
            a.interestedBranch=interestedBranch; a.preferredCollege=preferredCollege;
            a.studyPreference=studyPreference; a.goal=goal; a.bookedAt=bookedAt; a.meetingLink = meetingLink;
            return a;
            
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String v) { this.studentName = v; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String v) { this.studentEmail = v; }
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long v) { this.counselorId = v; }
    public String getCounselorName() { return counselorName; }
    public void setCounselorName(String v) { this.counselorName = v; }
    public String getSessionType() { return sessionType; }
    public void setSessionType(String v) { this.sessionType = v; }
    public String getDate() { return date; }
    public void setDate(String v) { this.date = v; }
    public String getTime() { return time; }
    public void setTime(String v) { this.time = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { this.phone = v; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String v) { this.fatherName = v; }
    public String getFatherPhone() { return fatherPhone; }
    public void setFatherPhone(String v) { this.fatherPhone = v; }
    public String getIntermediateGrade() { return intermediateGrade; }
    public void setIntermediateGrade(String v) { this.intermediateGrade = v; }
    public String getStream() { return stream; }
    public void setStream(String v) { this.stream = v; }
    public String getInterestedSkills() { return interestedSkills; }
    public void setInterestedSkills(String v) { this.interestedSkills = v; }
    public String getHobbies() { return hobbies; }
    public void setHobbies(String v) { this.hobbies = v; }
    public String getInterestedBranch() { return interestedBranch; }
    public void setInterestedBranch(String v) { this.interestedBranch = v; }
    public String getPreferredCollege() { return preferredCollege; }
    public void setPreferredCollege(String v) { this.preferredCollege = v; }
    public String getStudyPreference() { return studyPreference; }
    public void setStudyPreference(String v) { this.studyPreference = v; }
    public String getGoal() { return goal; }
    public void setGoal(String v) { this.goal = v; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime v) { this.bookedAt = v; }
    public String getMeetingLink() { return meetingLink; }
public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
}
