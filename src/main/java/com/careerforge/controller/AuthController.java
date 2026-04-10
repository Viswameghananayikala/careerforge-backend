package com.careerforge.controller;

import com.careerforge.model.User;
import com.careerforge.repository.UserRepository;
import com.careerforge.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    // POST /api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String name     = body.get("name");
        String email    = body.get("email");
        String password = body.get("password");

        if (name == null || email == null || password == null)
            return ResponseEntity.badRequest().body(Map.of("error", "name, email and password are required"));

        email = email.toLowerCase().trim();

        if (userRepo.existsByEmail(email))
            return ResponseEntity.status(409).body(Map.of("error", "Email already registered"));

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("user");
        userRepo.save(user);

        String token = jwtUtil.generateToken(email, "user");
        return ResponseEntity.ok(Map.of(
            "token", token,
            "role",  "user",
            "name",  name.trim(),
            "email", email
        ));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        if (email == null || password == null)
            return ResponseEntity.badRequest().body(Map.of("error", "email and password required"));

        email = email.toLowerCase().trim();
        Optional<User> opt = userRepo.findByEmail(email);

        if (opt.isEmpty())
            return ResponseEntity.status(404).body(Map.of("error", "No account found with this email"));

        User user = opt.get();
        if (!passwordEncoder.matches(password, user.getPassword()))
            return ResponseEntity.status(401).body(Map.of("error", "Incorrect password"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of(
            "token", token,
            "role",  user.getRole(),
            "name",  user.getName(),
            "email", user.getEmail()
        ));
    }
}
