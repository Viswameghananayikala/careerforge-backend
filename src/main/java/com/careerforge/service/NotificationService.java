package com.careerforge.service;

import com.careerforge.model.Notification;
import com.careerforge.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    public void send(String email, String message, String type) {
        Notification n = new Notification();

       n.setUserEmail(email);
       n.setMessage(message);
       n.setType(type);
       n.setRead(false);

        repo.save(n);
    }
}