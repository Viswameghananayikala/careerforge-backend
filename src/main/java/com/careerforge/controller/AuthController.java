package com.careerforge.controller;

import com.careerforge.model.User;
import com.careerforge.repository.UserRepository;
import com.careerforge.security.JwtUtil;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // ── POST /api/auth/signup ──────────────────────────────────────
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String name     = body.get("name");
        String email    = body.get("email");
        String password = body.get("password");

        if (name == null || email == null || password == null)
            return ResponseEntity.badRequest().body(Map.of("error", "name, email and password are required"));

        if (userRepo.existsByEmail(email.toLowerCase()))
return ResponseEntity.status(409)
    .body(Map.of("error", "An account with this email already exists. Please sign in."));
        User user = userRepo.save(User.builder()
                .name(name.trim())
                .email(email.toLowerCase().trim())
                .password(encoder.encode(password))
                .role(User.Role.USER)
                .build());

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole().name().toLowerCase(),
                "name", user.getName(),
                "email", user.getEmail()
        ));
    }

    // ── POST /api/auth/login ───────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        if (email == null || password == null)
            return ResponseEntity.badRequest().body(Map.of("error", "email and password required"));

        User user = userRepo.findByEmail(email.toLowerCase().trim())
                .orElse(null);

        if (user == null)
            return ResponseEntity.status(404).body(Map.of("error", "No account found with this email"));
        
        if (user.isBlocked())
    return ResponseEntity.status(403).body(
        Map.of("error", "Your account is blocked. Contact admin to get unblocked.")
    );

        if (!encoder.matches(password, user.getPassword()))
            return ResponseEntity.status(401).body(Map.of("error", "Incorrect password"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole().name().toLowerCase(),
                "name", user.getName(),
                "email", user.getEmail()
        ));
    }
}
