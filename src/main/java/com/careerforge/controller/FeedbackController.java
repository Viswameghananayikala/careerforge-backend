package com.careerforge.controller;

import com.careerforge.model.Feedback;
import com.careerforge.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired private FeedbackRepository feedbackRepo;

    // POST /api/feedback (auth required)
    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestBody Map<String, Object> body,
                                             Authentication auth) {
        String email = auth.getName();

        Feedback f = new Feedback();
        f.setStudentEmail(email);
        f.setStudentName(str(body, "studentName"));
        f.setCounselorId(body.get("counselorId") != null
                ? Long.valueOf(body.get("counselorId").toString()) : null);
        f.setCounselorName(str(body, "counselorName"));
        f.setAppointmentId(body.get("appointmentId") != null
                ? Long.valueOf(body.get("appointmentId").toString()) : null);
        f.setRating(body.get("rating") != null
                ? Integer.valueOf(body.get("rating").toString()) : null);
        f.setComment(str(body, "comment"));

        feedbackRepo.save(f);
        return ResponseEntity.ok(Map.of("message", "Feedback submitted successfully"));
    }

    private String str(Map<String, Object> body, String key) {
        Object val = body.get(key);
        return val != null ? val.toString() : null;
    }
}
