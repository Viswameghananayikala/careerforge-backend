package com.careerforge.controller;

import com.careerforge.model.Counselor;
import com.careerforge.repository.CounselorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/counselors")
public class CounselorController {

    @Autowired private CounselorRepository counselorRepo;

    // GET /api/counselors — public
    @GetMapping
    public ResponseEntity<?> getAllCounselors() {
        List<Counselor> counselors = counselorRepo.findAll();
        // Convert slots string to array (same as Express: c.slots.split(','))
        List<Map<String, Object>> result = new ArrayList<>();
        for (Counselor c : counselors) {
            result.add(toMap(c));
        }
        return ResponseEntity.ok(result);
    }

    // GET /api/counselors/:id — public
    @GetMapping("/{id}")
    public ResponseEntity<?> getCounselor(@PathVariable Long id) {
        Optional<Counselor> opt = counselorRepo.findById(id);
        if (opt.isEmpty())
            return ResponseEntity.status(404).body(Map.of("error", "Counselor not found"));
        return ResponseEntity.ok(toMap(opt.get()));
    }

    // Convert Counselor to Map with slots as List (same as Node response)
    private Map<String, Object> toMap(Counselor c) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id",             c.getId());
        map.put("name",           c.getName());
        map.put("title",          c.getTitle());
        map.put("specialization", c.getSpecialization());
        map.put("domain",         c.getDomain());
        map.put("exp",            c.getExp());
        map.put("rating",         c.getRating());
        map.put("sessions",       c.getSessions());
        map.put("avatar",         c.getAvatar());
        map.put("bio",            c.getBio());
        map.put("badge",          c.getBadge());
        map.put("resume",         c.getResume());
        map.put("price",          c.getPrice());
        map.put("created_at",     c.getCreatedAt());

        // slots: "Mon 10AM,Wed 2PM" → ["Mon 10AM", "Wed 2PM"]
        if (c.getSlots() != null && !c.getSlots().isBlank()) {
            List<String> slotList = Arrays.stream(c.getSlots().split(","))
                    .map(String::trim)
                    .toList();
            map.put("slots", slotList);
        } else {
            map.put("slots", List.of());
        }
        return map;
    }
}
