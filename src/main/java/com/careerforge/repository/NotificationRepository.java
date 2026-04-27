package com.careerforge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.careerforge.model.Notification;



public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserEmailOrderByCreatedAtDesc(String email);

    boolean existsByUserEmailAndMessage(String email, String message);
}
