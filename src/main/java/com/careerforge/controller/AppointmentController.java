package com.careerforge.controller;

import com.careerforge.model.Appointment;
import com.careerforge.repository.AppointmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.careerforge.model.User;
import com.careerforge.model.Counselor;
import com.careerforge.repository.UserRepository;
import com.careerforge.repository.CounselorRepository;
import com.careerforge.model.AvailabilitySlot;
import com.careerforge.repository.AvailabilitySlotRepository;
import com.careerforge.repository.FeedbackRepository;
import com.careerforge.service.NotificationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
private final AvailabilitySlotRepository slotRepo;
   private final AppointmentRepository apptRepo;
private final UserRepository userRepo;
private final CounselorRepository counselorRepo;
private final FeedbackRepository feedbackRepo;
@Autowired
private NotificationService notifService;

public AppointmentController(
    AppointmentRepository apptRepo,
    UserRepository userRepo,
    CounselorRepository counselorRepo,
    AvailabilitySlotRepository slotRepo,
    FeedbackRepository feedbackRepo  
) {
    this.apptRepo = apptRepo;
    this.userRepo = userRepo;
    this.counselorRepo = counselorRepo;
    this.slotRepo = slotRepo;
    this.feedbackRepo = feedbackRepo;
}
    // POST /api/appointments — book a session
    @PostMapping
    public ResponseEntity<?> book(@RequestBody Map<String, Object> body, Authentication auth) {
        String email = auth.getName();
Long counselorId = longVal(body, "counselorId");
    String date = str(body, "date");
    String time = str(body, "time");

    // 🔥 CHECK SLOT EXISTS
    AvailabilitySlot slot = slotRepo.findByCounselorIdAndDateAndTime(
        counselorId, date, time
    );

    if (slot == null) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Invalid slot selected"
        ));
    }

    // 🔥 PREVENT DOUBLE BOOKING
    if (slot.isBooked()) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Slot already booked"
        ));
    }   

        Appointment appt = Appointment.builder()
                .studentEmail(email)
                .studentName(str(body, "studentName"))
                .counselorId(longVal(body, "counselorId"))
                .counselorName(str(body, "counselorName"))
                .sessionType(str(body, "sessionType"))
                .date(str(body, "date"))
                .time(str(body, "time"))
                .phone(str(body, "phone"))
                .fatherName(str(body, "fatherName"))
                .fatherPhone(str(body, "fatherPhone"))
                .intermediateGrade(str(body, "intermediateGrade"))
                .stream(str(body, "stream"))
                .interestedSkills(str(body, "interestedSkills"))
                .hobbies(str(body, "hobbies"))
                .interestedBranch(str(body, "interestedBranch"))
                .preferredCollege(str(body, "preferredCollege"))
                .studyPreference(str(body, "studyPreference"))
                .goal(str(body, "goal"))
                .build();

        Appointment saved = apptRepo.save(appt);
        notifService.send(
    email,
    "Session booked with " + str(body, "counselorName"),
    "success"
);


        // 🔥 UPDATE SESSIONS COUNT
Counselor counselor = counselorRepo.findById(counselorId).orElse(null);


if (counselor != null) {
    int totalSessions = apptRepo.findByCounselorId(counselorId).size();
    counselor.setSessions(totalSessions);
    counselorRepo.save(counselor);
     // 🔥 GET COUNSELOR USER
    User counselorUser = userRepo.findById(counselor.getUserId()).orElse(null);

    if (counselorUser != null) {

        // 🔔 SEND NOTIFICATION TO COUNSELOR
        notifService.send(
            counselorUser.getEmail(),
            "New session booked by " + str(body, "studentName"),
            "info"
        );
    }
    
}

        // ✅ VERY IMPORTANT
slot.setBooked(true);
slotRepo.save(slot);

        return ResponseEntity.ok(Map.of(
                "id", "S" + String.format("%03d", saved.getId()),
                "message", "Session booked successfully",
                "status", "pending"
        ));
    }

    // GET /api/appointments/my — user's appointments
    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> myAppointments(Authentication auth) {
       List<Appointment> list = apptRepo.findByStudentEmail(auth.getName());

autoExpire(list); // 🔥 ADD THIS

return ResponseEntity.ok(list);
    }

    private String str(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? v.toString() : null;
    }

    private Long longVal(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        return Long.valueOf(v.toString());
    }

@GetMapping("/counselor")
public ResponseEntity<List<Appointment>> counselorAppointments(Authentication auth) {

    String email = auth.getName();

    // 🔥 get counselor user
    User user = userRepo.findByEmail(email).orElse(null);

    if (user == null) {
        return ResponseEntity.badRequest().build();
    }

    // 🔥 find counselor profile
    Counselor counselor = counselorRepo.findByUserId(user.getId());

    if (counselor == null) {
        return ResponseEntity.ok(List.of());
    }

    // ✅ ONLY HIS APPOINTMENTS
    List<Appointment> list =
    apptRepo.findByCounselorId(counselor.getId());

autoExpire(list); // 🔥 ADD THIS

return ResponseEntity.ok(list);
}
@PutMapping("/{id}/status")
public ResponseEntity<?> updateStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> body
) {
    Appointment appt = apptRepo.findById(id).orElse(null);

    if (appt == null) {
        return ResponseEntity.status(404).body(Map.of("error", "Not found"));
    }

    String status = body.get("status");

    if (status == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "Status required"));
    }

    appt.setStatus(status); // accepted / rejected
    if ("accepted".equalsIgnoreCase(status)) {
    appt.setMeetingLink("https://meet.jit.si/careerforge-" + System.currentTimeMillis());

    notifService.send(
        appt.getStudentEmail(),
        "Your session with " + appt.getCounselorName() + " has been accepted",
        "success"
    );

}
    apptRepo.save(appt);

    return ResponseEntity.ok(Map.of("message", "Updated"));
}
private void autoExpire(List<Appointment> list) {
    LocalDateTime now = LocalDateTime.now();

    for (Appointment a : list) {
        if ("pending".equalsIgnoreCase(a.getStatus())) {
            try {
                // 🔥 FIX: handle AM/PM format
                String time = a.getTime().toUpperCase().replaceAll("\\s+", "");

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

                LocalDateTime sessionTime = LocalDateTime.parse(
                        a.getDate() + " " + time,
                        formatter
                );

                if (sessionTime.isBefore(now)) {
    a.setStatus("expired");

    AvailabilitySlot slot = slotRepo.findByCounselorIdAndDateAndTime(
        a.getCounselorId(), a.getDate(), a.getTime()
    );

    if (slot != null) {
        slot.setBooked(false); // 🔥 free slot again
        slotRepo.save(slot);
    }

    apptRepo.save(a);
}

            } catch (Exception e) {
                System.out.println("❌ Time parse error: " + e.getMessage());
            }
        }
    }
}
@PutMapping("/{id}/reschedule")
public ResponseEntity<?> reschedule(
        @PathVariable Long id,
        @RequestBody Map<String, String> body
) {
    Appointment appt = apptRepo.findById(id).orElse(null);

    if (appt == null) {
        return ResponseEntity.status(404).body("Not found");
    }

    String newDate = body.get("date");
    String newTime = body.get("time");

    // 🔥 old slot free
    AvailabilitySlot oldSlot = slotRepo.findByCounselorIdAndDateAndTime(
        appt.getCounselorId(),
        appt.getDate(),
        appt.getTime()
    );

    if (oldSlot != null) {
        oldSlot.setBooked(false);
        slotRepo.save(oldSlot);
    }

    // 🔥 new slot check
    AvailabilitySlot newSlot = slotRepo.findByCounselorIdAndDateAndTime(
        appt.getCounselorId(),
        newDate,
        newTime
    );

    if (newSlot == null || newSlot.isBooked()) {
        return ResponseEntity.badRequest().body("Slot unavailable");
    }

    // 🔥 update
    appt.setDate(newDate);
    appt.setTime(newTime);

    appt.setMeetingLink("https://meet.jit.si/careerforge-" + System.currentTimeMillis());
    apptRepo.save(appt);

    newSlot.setBooked(true);
    slotRepo.save(newSlot);

    return ResponseEntity.ok(Map.of("message", "Rescheduled"));
}
}
