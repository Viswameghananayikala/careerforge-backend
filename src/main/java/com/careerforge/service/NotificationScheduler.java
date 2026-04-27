package com.careerforge.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.careerforge.model.Appointment;
import com.careerforge.repository.AppointmentRepository;
import com.careerforge.repository.NotificationRepository;

@Component
public class NotificationScheduler {

    @Autowired
    private AppointmentRepository apptRepo;

    @Autowired
    private NotificationService notifService;

    @Autowired
    private NotificationRepository notifRepo;

    @Scheduled(fixedRate = 60000)
    public void checkSessions() {

          System.out.println("🔥 SCHEDULER RUNNING...");
        List<Appointment> list = apptRepo.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Appointment a : list) {

            if (!"accepted".equalsIgnoreCase(a.getStatus())) continue;

            try {
String time = a.getTime()
        .trim()
        .toUpperCase()
        .replaceAll("\\s+", " ");

               System.out.println("RAW TIME: '" + time + "'");
    System.out.println("DATE: " + a.getDate());

    DateTimeFormatter formatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a", java.util.Locale.ENGLISH);
 
                LocalDateTime sessionTime =
                        LocalDateTime.parse(a.getDate() + " " + time, formatter);

                long minutes = Duration.between(now, sessionTime).toMinutes();
                    System.out.println("🕒 Minutes left: " + minutes);


                if (minutes <= 15 && minutes >= 14) {

    String message =
        "Session with " + a.getCounselorName() + " starts in 15 minutes";

    boolean already =
        notifRepo.existsByUserEmailAndMessage(
            a.getStudentEmail(),
            message
        );

    if (!already) {
        notifService.send(
            a.getStudentEmail(),
            message,
            "warning"
        );
                    System.out.println("✅ Notification saved!");

    }
}

            } catch (Exception e) {
                    System.out.println("❌ PARSE FAILED:");

                e.printStackTrace();

            }
        }
    }
}