package com.careerforge.controller;

import com.careerforge.model.Feedback;
import com.careerforge.repository.FeedbackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.careerforge.repository.AppointmentRepository;
import com.careerforge.model.Appointment;
import com.careerforge.model.Counselor;
import com.careerforge.repository.CounselorRepository;
import com.careerforge.model.User;
import com.careerforge.repository.UserRepository;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
 
    private final FeedbackRepository feedbackRepo;
   private final AppointmentRepository apptRepo;
    private final CounselorRepository counselorRepo;
    private final UserRepository userRepo;


    public FeedbackController(FeedbackRepository feedbackRepo,    AppointmentRepository apptRepo, CounselorRepository counselorRepo,    UserRepository userRepo
) {
        this.feedbackRepo = feedbackRepo;
            this.apptRepo = apptRepo;
            this.counselorRepo=counselorRepo;
                this.userRepo = userRepo;


    }

    // POST /api/feedback
   @PostMapping
public ResponseEntity<?> submit(@RequestBody Map<String, Object> body, Authentication auth) {

    Long appointmentId = longVal(body, "appointmentId");

    // 🔥 GET APPOINTMENT
    Appointment appt = apptRepo.findById(appointmentId).orElse(null);

    if (appt == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid appointment"));
    }

    // ❌ BLOCK IF NOT ACCEPTED
    if (!"accepted".equalsIgnoreCase(appt.getStatus())) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "You can only rate accepted sessions"));
    }

    // ❌ CHECK DUPLICATE
    if (feedbackRepo.findByAppointmentId(appointmentId).isPresent()) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Already rated"));
    }

    Feedback fb = Feedback.builder()
            .studentEmail(auth.getName())
            .studentName(str(body, "studentName"))
            .counselorId(longVal(body, "counselorId"))
            .counselorName(str(body, "counselorName"))
            .appointmentId(appointmentId)
            .rating(intVal(body, "rating"))
            .comment(str(body, "comment"))
            .build();

    feedbackRepo.save(fb);
    // 🔥 CALCULATE NEW AVERAGE RATING
double avgRating = feedbackRepo.findByCounselorId(fb.getCounselorId())
        .stream()
        .mapToInt(Feedback::getRating)
        .average()
        .orElse(0.0);

// 🔥 UPDATE COUNSELOR
Counselor counselor = counselorRepo.findById(fb.getCounselorId()).orElse(null);

if (counselor != null) {
    counselor.setRating(avgRating);
    counselorRepo.save(counselor);
}

    return ResponseEntity.ok(Map.of("message", "Feedback submitted","rating",avgRating));
}

    private String str(Map<String, Object> m, String key) {
        Object v = m.get(key); return v != null ? v.toString() : null;
    }
    private Long longVal(Map<String, Object> m, String key) {
        Object v = m.get(key); return v != null ? Long.valueOf(v.toString()) : null;
    }
    private Integer intVal(Map<String, Object> m, String key) {
        Object v = m.get(key); return v != null ? Integer.valueOf(v.toString()) : null;
    }
    @GetMapping("/my")
public ResponseEntity<?> myFeedback(Authentication auth) {
    return ResponseEntity.ok(
        feedbackRepo.findByStudentEmail(auth.getName())
    );
}
@GetMapping("/counselor")
public ResponseEntity<?> counselorFeedback(Authentication auth) {

    String email = auth.getName();

    User user = userRepo.findByEmail(email).orElse(null);

    if (user == null) {
        return ResponseEntity.badRequest().build();
    }

    Counselor counselor = counselorRepo.findByUserId(user.getId());

    if (counselor == null) {
        return ResponseEntity.ok(List.of());
    }

    return ResponseEntity.ok(
        feedbackRepo.findByCounselorId(counselor.getId())
    );
}
}
