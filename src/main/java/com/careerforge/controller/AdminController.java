package com.careerforge.controller;

import com.careerforge.model.*;
import com.careerforge.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.util.stream.Collectors; // ✅ IMPORTANT


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final AppointmentRepository apptRepo;
    private final FeedbackRepository feedbackRepo;
    private final CounselorRepository counselorRepo;
    private final PasswordEncoder encoder;
    public AdminController(UserRepository userRepo, AppointmentRepository apptRepo,
                           FeedbackRepository feedbackRepo, CounselorRepository counselorRepo,PasswordEncoder encoder) {
        this.userRepo      = userRepo;
        this.apptRepo      = apptRepo;
        this.feedbackRepo  = feedbackRepo;
        this.counselorRepo = counselorRepo;
        this.encoder = encoder;
    }

    // GET /api/admin/users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    // GET /api/admin/appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(apptRepo.findAll());
    }

    // GET /api/admin/feedback
    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackRepo.findAll());
    }

    // POST /api/admin/counselors — add new counselor
    @Transactional
   @PostMapping("/counselors")
public ResponseEntity<?> addCounselor(@RequestBody Map<String, Object> body) {
        System.out.println("🔥 API HIT: /admin/counselors"); // 👈 ADD THIS


    String name = str(body, "name");
    String email = str(body, "email");
    String password = str(body, "password");

    if (name == null || email == null || password == null)
        return ResponseEntity.badRequest().body(Map.of("error", "Name, email, password required"));

    if (userRepo.existsByEmail(email.toLowerCase()))
        return ResponseEntity.status(409).body(Map.of("error", "Email already exists"));

    // ✅ 1. CREATE LOGIN USER
    System.out.println("👉 Creating user: " + email);

User user = userRepo.save(User.builder()
        .name(name)
        .email(email.toLowerCase())
        .password(encoder.encode(password))
        .role(User.Role.COUNSELOR)
        .build());

System.out.println("✅ User saved ID: " + user.getId());

    String slots = body.containsKey("slots") ? body.get("slots").toString() : "";

    String initials = name != null
            ? Arrays.stream(name.split(" "))
                .map(w -> String.valueOf(w.charAt(0)).toUpperCase())
                .reduce("", String::concat)
                .substring(0, Math.min(2, name.split(" ").length))
            : "CF";

    // ✅ 2. CREATE COUNSELOR PROFILE
    Counselor c = Counselor.builder()
            .userId(user.getId()) // 🔥 LINK USER
            .name(name)
            .title(str(body, "title"))
            .specialization(str(body, "specialization"))
            .domain(str(body, "domain"))
            .exp(str(body, "exp"))
            .bio(str(body, "bio"))
            .slots(slots)
            .avatar(initials)
            .rating(0.0)
            .sessions(0)
            .price("Free")
            .badge("badge-teal")
            .resume("#")
            .build();

    counselorRepo.save(c);

return ResponseEntity.ok(Map.of(
    "message", "Counselor added",
    "userId", user.getId(),
    "counselorId", c.getId()
));}
    // GET /api/admin/stats
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long totalUsers    = userRepo.count();
        long totalSessions = apptRepo.count();
        long totalCounselors = counselorRepo.count();
        double avgRating = feedbackRepo.findAll().stream()
                .mapToInt(Feedback::getRating).average().orElse(0.0);

        return ResponseEntity.ok(Map.of(
                "totalStudents",   totalUsers,
                "totalSessions",   totalSessions,
                "totalCounselors", totalCounselors,
                "avgRating",       Math.round(avgRating * 10.0) / 10.0,
                "totalFeedback",   feedbackRepo.count()
        ));
    }
 // ✅ GET COUNSELORS (🔥 UPDATED)
    @GetMapping("/counselors")
    public ResponseEntity<?> getAllCounselors() {

        List<Map<String, Object>> result = counselorRepo.findAll().stream().map(c -> {

            User user = userRepo.findById(c.getUserId()).orElse(null);

            Map<String, Object> map = new HashMap<>();

            map.put("id", c.getId());
            map.put("userId", c.getUserId());
            map.put("name", c.getName());
            map.put("title", c.getTitle());
            map.put("specialization", c.getSpecialization());
            map.put("domain", c.getDomain());
            map.put("exp", c.getExp());
            map.put("rating", c.getRating());
            map.put("sessions", c.getSessions());
            map.put("avatar", c.getAvatar());
            map.put("bio", c.getBio());
            map.put("slots", c.getSlots());
            map.put("badge", c.getBadge());
            map.put("price", c.getPrice());

            // ✅ IMPORTANT
            map.put("email", user != null ? user.getEmail() : "N/A");
            map.put("blocked", user != null && user.isBlocked());

            return map;

        }).collect(Collectors.toList()); // ✅ FIXED

        return ResponseEntity.ok(result);
    }

@PutMapping("/counselors/{id}")
@Transactional
public ResponseEntity<?> updateCounselor(
        @PathVariable Long id,
        @RequestBody Map<String, Object> body) {

    Counselor c = counselorRepo.findById(id).orElse(null);
    if (c == null)
        return ResponseEntity.status(404).body(Map.of("error", "Counselor not found"));

    // 🔥 UPDATE COUNSELOR TABLE
    c.setName(str(body, "name"));
    c.setTitle(str(body, "title"));
    c.setSpecialization(str(body, "specialization"));
    c.setDomain(str(body, "domain"));
    c.setExp(str(body, "exp"));
    c.setBio(str(body, "bio"));
    c.setSlots(str(body, "slots"));
    c.setAvatar(str(body, "avatar"));
    c.setBadge(str(body, "badge"));
    c.setResume(str(body, "resume"));
    c.setPrice(str(body, "price"));

    if (body.get("rating") != null)
        c.setRating(Double.valueOf(body.get("rating").toString()));

    if (body.get("sessions") != null)
        c.setSessions(Integer.valueOf(body.get("sessions").toString()));

    counselorRepo.save(c);
    
    return ResponseEntity.ok(Map.of("message", "✅ Counselor fully updated"));
}

@PutMapping("/users/{id}/block")
public ResponseEntity<?> toggleBlock(@PathVariable Long id) {

    User user = userRepo.findById(id).orElse(null);

    if (user == null)
        return ResponseEntity.status(404).body(Map.of("error", "User not found"));

    user.setBlocked(!user.isBlocked());
    userRepo.save(user);

    return ResponseEntity.ok(Map.of(
        "message", user.isBlocked() ? "User blocked" : "User unblocked"
    ));
}

@DeleteMapping("/feedback/{id}")
public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {

    if (!feedbackRepo.existsById(id)) {
        return ResponseEntity.status(404).body(Map.of("error", "Feedback not found"));
    }

    feedbackRepo.deleteById(id);

    return ResponseEntity.ok(Map.of("message", "Feedback deleted"));
}

    // POST /api/admin/users
@PostMapping("/users")
public ResponseEntity<?> addUser(@RequestBody Map<String, String> body) {

    String name = body.get("name");
    String email = body.get("email");
    String password = body.get("password");

    if (name == null || email == null || password == null)
        return ResponseEntity.badRequest().body(Map.of("error", "All fields required"));

    if (userRepo.existsByEmail(email.toLowerCase()))
        return ResponseEntity.status(409).body(Map.of("error", "Email already exists"));

    User user = userRepo.save(User.builder()
            .name(name)
            .email(email.toLowerCase())
            .password(encoder.encode(password)) // ✅ FIX
            .role(User.Role.USER)
            .build());

    return ResponseEntity.ok(user);
}

    private String str(Map<String, Object> m, String key) {
        Object v = m.get(key); return v != null ? v.toString() : null;
    }
}
