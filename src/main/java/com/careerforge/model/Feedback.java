package com.careerforge.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentEmail;
    private String studentName;
    private Long counselorId;
    private String counselorName;
    @Column(unique = true)
private Long appointmentId;
    private Integer rating;

    @Column(length = 2000)
    private String comment;

    private LocalDate date;

    @PrePersist
    protected void onCreate() {
        if (date == null) date = LocalDate.now();
    }

    public Feedback() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String studentEmail; private String studentName;
        private Long counselorId; private String counselorName; private Long appointmentId;
        private Integer rating; private String comment; private LocalDate date;

        public Builder id(Long v) { this.id=v; return this; }
        public Builder studentEmail(String v) { this.studentEmail=v; return this; }
        public Builder studentName(String v) { this.studentName=v; return this; }
        public Builder counselorId(Long v) { this.counselorId=v; return this; }
        public Builder counselorName(String v) { this.counselorName=v; return this; }
        public Builder appointmentId(Long v) { this.appointmentId=v; return this; }
        public Builder rating(Integer v) { this.rating=v; return this; }
        public Builder comment(String v) { this.comment=v; return this; }
        public Builder date(LocalDate v) { this.date=v; return this; }

        public Feedback build() {
            Feedback f = new Feedback();
            f.id=id; f.studentEmail=studentEmail; f.studentName=studentName;
            f.counselorId=counselorId; f.counselorName=counselorName;
            f.appointmentId=appointmentId; f.rating=rating; f.comment=comment; f.date=date;
            return f;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String v) { this.studentEmail = v; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String v) { this.studentName = v; }
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long v) { this.counselorId = v; }
    public String getCounselorName() { return counselorName; }
    public void setCounselorName(String v) { this.counselorName = v; }
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long v) { this.appointmentId = v; }
    public Integer getRating() { return rating; }
    public void setRating(Integer v) { this.rating = v; }
    public String getComment() { return comment; }
    public void setComment(String v) { this.comment = v; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate v) { this.date = v; }
}
