package com.careerforge.repository;

import com.careerforge.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double findAverageRating();
}
