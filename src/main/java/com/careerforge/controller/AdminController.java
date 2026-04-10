package com.careerforge.controller;

import com.careerforge.model.Counselor;
import com.careerforge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private UserRepository        userRepo;
    @Autowired private AppointmentRepository appointmentRepo;
    @Autowired private FeedbackRepository    feedbackRepo;
    @Autowired private CounselorRepository   counselorRepo;

    // GET /api/admin/users
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        // Return only safe fields (no password) - same as Node
        var users = userRepo.findAll().stream().map(u -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",         u.getId());
            m.put("name",       u.getName());
            m.put("email",      u.getEmail());
            m.put("role",       u.getRole());
            m.put("joined_at",  u.getJoinedAt());
            m.put("created_at", u.getCreatedAt());
            return m;
        }).toList();
        return ResponseEntity.ok(users);
    }

    // GET /api/admin/appointments
    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments() {
        var list = appointmentRepo.findAll()
                .stream()
                .sorted((a, b) -> b.getBookedAt().compareTo(a.getBookedAt()))
                .toList();
        return ResponseEntity.ok(list);
    }

    // GET /api/admin/feedback
    @GetMapping("/feedback")
    public ResponseEntity<?> getFeedback() {
        var list = feedbackRepo.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(list);
    }

    // GET /api/admin/stats
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long totalStudents   = userRepo.countByRole("user");
        long totalSessions   = appointmentRepo.count();
        long totalCounselors = counselorRepo.count();
        long totalFeedback   = feedbackRepo.count();
        Double avgRaw        = feedbackRepo.findAverageRating();
        double avgRating     = avgRaw != null ? Math.round(avgRaw * 10.0) / 10.0 : 0.0;

        return ResponseEntity.ok(Map.of(
            "totalStudents",   totalStudents,
            "totalSessions",   totalSessions,
            "totalCounselors", totalCounselors,
            "totalFeedback",   totalFeedback,
            "avgRating",       avgRating
        ));
    }

    // POST /api/admin/counselors — add new counselor
    @PostMapping("/counselors")
    public ResponseEntity<?> addCounselor(@RequestBody Map<String, String> body) {
        String name   = body.get("name");
        String title  = body.get("title");
        String spec   = body.get("specialization");
        String domain = body.get("domain");
        String exp    = body.get("exp");
        String bio    = body.get("bio");
        String slots  = body.get("slots");

        // Generate avatar initials same as Express
        String avatar = "CF";
        if (name != null && !name.isBlank()) {
            String[] parts = name.trim().split(" ");
            StringBuilder sb = new StringBuilder();
            for (String p : parts) { if (!p.isEmpty()) sb.append(p.charAt(0)); }
            avatar = sb.toString().toUpperCase().substring(0, Math.min(2, sb.length()));
        }

        Counselor c = new Counselor();
        c.setName(name);
        c.setTitle(title);
        c.setSpecialization(spec);
        c.setDomain(domain);
        c.setExp(exp);
        c.setBio(bio);
        c.setSlots(slots);
        c.setAvatar(avatar);
        c.setRating(5.0);
        c.setSessions(0);
        c.setPrice("Free");
        c.setBadge("badge-teal");
        c.setResume("#");

        Counselor saved = counselorRepo.save(c);
        return ResponseEntity.ok(saved);
    }
}
