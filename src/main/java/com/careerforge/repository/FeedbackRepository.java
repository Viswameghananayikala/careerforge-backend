package com.careerforge.repository;

import com.careerforge.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStudentEmail(String email);
    List<Feedback> findByCounselorId(Long counselorId);
    Optional<Feedback> findByAppointmentId(Long appointmentId);
}
