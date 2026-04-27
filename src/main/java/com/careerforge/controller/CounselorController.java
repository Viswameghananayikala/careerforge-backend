package com.careerforge.controller;

import com.careerforge.model.Counselor;
import com.careerforge.repository.CounselorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.careerforge.model.User;
import com.careerforge.repository.UserRepository;
import org.springframework.security.core.Authentication;

import com.careerforge.repository.FeedbackRepository;
import com.careerforge.repository.AppointmentRepository;
import com.careerforge.model.Feedback;
import com.careerforge.model.Appointment;

import java.util.*;

@RestController
@RequestMapping("/api/counselors")
public class CounselorController {

    private final CounselorRepository counselorRepo;
     private final UserRepository userRepo;
     private final FeedbackRepository feedbackRepo;
private final AppointmentRepository apptRepo;

    public CounselorController(CounselorRepository counselorRepo,    UserRepository userRepo,FeedbackRepository feedbackRepo,
    AppointmentRepository apptRepo
) {
        this.counselorRepo = counselorRepo;
            this.userRepo = userRepo;
            this.feedbackRepo = feedbackRepo;
    this.apptRepo = apptRepo;

    }

    // GET /api/counselors — public
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Counselor> list = counselorRepo.findAll();
        // convert slots string → list for frontend
        List<Map<String, Object>> result = list.stream().map(c -> {
            Map<String, Object> m = new LinkedHashMap<>();

              User user = userRepo.findById(c.getUserId()).orElse(null);
              List<Feedback> feedbacks = feedbackRepo.findByCounselorId(c.getId());

// ⭐ CALCULATE RATING
double avgRating = feedbacks.isEmpty() ? 0 :
        feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0);

// 📅 GET APPOINTMENTS
List<Appointment> appts = apptRepo.findByCounselorId(c.getId());

// 📊 TOTAL SESSIONS
int totalSessions = appts.size();

            m.put("id", c.getId());
            m.put("name", c.getName());
            m.put("email", user != null ? user.getEmail() : null);
            m.put("title", c.getTitle());
            m.put("specialization", c.getSpecialization());
            m.put("domain", c.getDomain());
            m.put("exp", c.getExp());
            m.put("rating", avgRating);
            m.put("sessions", totalSessions);
            m.put("avatar", c.getAvatar());
            m.put("bio", c.getBio());
            m.put("price", c.getPrice());
            m.put("badge", c.getBadge());
            m.put("resume", c.getResume());
            m.put("slots", c.getSlots() != null
                    ? Arrays.asList(c.getSlots().split(","))
                    : List.of());
            return m;
        }).toList();
        return ResponseEntity.ok(result);
    }

    // GET /api/counselors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return counselorRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
public ResponseEntity<?> updateMyProfile(
        @RequestBody Map<String, Object> body,
        Authentication auth
) {
    String email = auth.getName();

    // 🔥 GET USER
    User user = userRepo.findByEmail(email).orElse(null);
    if (user == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    }

    // 🔥 GET COUNSELOR
    Counselor c = counselorRepo.findByUserId(user.getId());
    if (c == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "Counselor profile not found"));
    }

    // ✅ SAFE UPDATE (ONLY IF PRESENT)
    if (body.get("name") != null)
        c.setName(body.get("name").toString());

    if (body.get("title") != null)
        c.setTitle(body.get("title").toString());

    if (body.get("specialization") != null)
        c.setSpecialization(body.get("specialization").toString());

    if (body.get("domain") != null)
        c.setDomain(body.get("domain").toString());

    if (body.get("exp") != null)
        c.setExp(body.get("exp").toString());

    if (body.get("bio") != null)
        c.setBio(body.get("bio").toString());

    if (body.get("slots") != null)
        c.setSlots(body.get("slots").toString());

    if (body.get("avatar") != null)
        c.setAvatar(body.get("avatar").toString());

    if (body.get("badge") != null)
        c.setBadge(body.get("badge").toString());

    if (body.get("resume") != null)
        c.setResume(body.get("resume").toString());

    if (body.get("price") != null)
        c.setPrice(body.get("price").toString());

    // 🔥 OPTIONAL (ONLY IF YOU WANT COUNSELOR TO EDIT)
    if (body.get("rating") != null)
        c.setRating(Double.valueOf(body.get("rating").toString()));

    if (body.get("sessions") != null)
        c.setSessions(Integer.valueOf(body.get("sessions").toString()));

    counselorRepo.save(c);

    return ResponseEntity.ok(Map.of(
            "message", "✅ Profile updated successfully",
            "counselorId", c.getId()
    ));
}
}
