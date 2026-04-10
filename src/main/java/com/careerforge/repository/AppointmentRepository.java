package com.careerforge.repository;

import com.careerforge.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStudentEmailOrderByBookedAtDesc(String email);
}
