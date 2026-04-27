package com.careerforge.controller;

import com.careerforge.model.AvailabilitySlot;
import com.careerforge.model.User;
import com.careerforge.model.Counselor;
import com.careerforge.repository.AvailabilitySlotRepository;
import com.careerforge.repository.UserRepository;
import com.careerforge.repository.CounselorRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
@RequestMapping("/api/slots")
public class AvailabilitySlotController {

    private final AvailabilitySlotRepository slotRepo;
    private final UserRepository userRepo;
    private final CounselorRepository counselorRepo;

    public AvailabilitySlotController(
        AvailabilitySlotRepository slotRepo,
        UserRepository userRepo,
        CounselorRepository counselorRepo
    ) {
        this.slotRepo = slotRepo;
        this.userRepo = userRepo;
        this.counselorRepo = counselorRepo;
    }

    // ✅ ADD SLOTS (Counselor)
    @PostMapping
    public ResponseEntity<?> addSlots(
        @RequestBody Map<String, Object> body,
        Authentication auth
    ) {
        String email = auth.getName();

        User user = userRepo.findByEmail(email).orElse(null);
        Counselor counselor = counselorRepo.findByUserId(user.getId());

        if (counselor == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Counselor not found"));
        }

        String date = body.get("date").toString();
        List<String> times = (List<String>) body.get("times");

        List<AvailabilitySlot> created = new ArrayList<>();

        for (String time : times) {
            AvailabilitySlot slot = new AvailabilitySlot();
            slot.setCounselorId(counselor.getId());
            slot.setDate(date);
            slot.setTime(time);
            slot.setBooked(false);

            created.add(slotRepo.save(slot));
        }

        return ResponseEntity.ok(created);
    }

    // ✅ GET SLOTS (User)
    @GetMapping("/{counselorId}")
    public ResponseEntity<?> getSlots(@PathVariable Long counselorId) {
        return ResponseEntity.ok(
            slotRepo.findByCounselorId(counselorId)
        );
    }

    @GetMapping("/me")
public ResponseEntity<?> mySlots(Authentication auth) {

    String email = auth.getName();

    User user = userRepo.findByEmail(email).orElse(null);

    if (user == null) {
        return ResponseEntity.badRequest().body("User not found");
    }

    Counselor counselor = counselorRepo.findByUserId(user.getId());

    if (counselor == null) {
        return ResponseEntity.ok(List.of());
    }

    return ResponseEntity.ok(
        slotRepo.findByCounselorId(counselor.getId())
    );
}
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteSlot(
        @PathVariable Long id,
        Authentication auth
) {
    String email = auth.getName();

    User user = userRepo.findByEmail(email).orElse(null);

    if (user == null) {
        return ResponseEntity.badRequest().body("User not found");
    }

    Counselor counselor = counselorRepo.findByUserId(user.getId());

    if (counselor == null) {
        return ResponseEntity.badRequest().body("Counselor not found");
    }

    AvailabilitySlot slot = slotRepo.findById(id).orElse(null);

    if (slot == null) {
        return ResponseEntity.status(404).body("Slot not found");
    }

    // 🔥 SECURITY CHECK (VERY IMPORTANT)
    if (!slot.getCounselorId().equals(counselor.getId())) {
        return ResponseEntity.status(403).body("Not allowed");
    }

    slotRepo.delete(slot);

    return ResponseEntity.ok(Map.of("message", "Deleted"));
}

}