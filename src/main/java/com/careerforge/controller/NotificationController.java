package com.careerforge.controller;

import com.careerforge.model.Notification;
import com.careerforge.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository repo;

    @GetMapping
    public List<Notification> get(Authentication auth) {
        return repo.findByUserEmailOrderByCreatedAtDesc(auth.getName());
    }

    @PutMapping("/read")
    public void markAllRead(Authentication auth) {
        List<Notification> list =
                repo.findByUserEmailOrderByCreatedAtDesc(auth.getName());

        list.forEach(n -> n.setRead(true));
        repo.saveAll(list);
    }

    @DeleteMapping
    public void clear(Authentication auth) {
        List<Notification> list =
                repo.findByUserEmailOrderByCreatedAtDesc(auth.getName());

        repo.deleteAll(list);
    }
}