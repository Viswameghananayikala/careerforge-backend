package com.careerforge.controller;

import com.careerforge.model.Appointment;
import com.careerforge.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired private AppointmentRepository appointmentRepo;

    // POST /api/appointments — book a session (auth required)
    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, Object> body,
                                              Authentication auth) {
        String email = auth.getName(); // extracted from JWT

        Appointment a = new Appointment();
        a.setStudentEmail(email);
        a.setStudentName(str(body, "studentName"));
        a.setCounselorId(body.get("counselorId") != null
                ? Long.valueOf(body.get("counselorId").toString()) : null);
        a.setCounselorName(str(body, "counselorName"));
        a.setSessionType(str(body, "sessionType"));
        a.setDate(str(body, "date"));
        a.setTime(str(body, "time"));
        a.setPhone(str(body, "phone"));
        a.setFatherName(str(body, "fatherName"));
        a.setFatherPhone(str(body, "fatherPhone"));
        a.setIntermediateGrade(str(body, "intermediateGrade"));
        a.setStream(str(body, "stream"));
        a.setInterestedSkills(str(body, "interestedSkills"));
        a.setHobbies(str(body, "hobbies"));
        a.setInterestedBranch(str(body, "interestedBranch"));
        a.setPreferredCollege(str(body, "preferredCollege"));
        a.setStudyPreference(str(body, "studyPreference"));
        a.setGoal(str(body, "goal"));
        a.setStatus("pending");

        Appointment saved = appointmentRepo.save(a);

        // Return same format as Express: S001, S002 etc.
        String id = "S" + String.format("%03d", saved.getId());
        return ResponseEntity.ok(Map.of(
            "id",      id,
            "message", "Session booked successfully",
            "status",  "pending"
        ));
    }

    // GET /api/appointments/my — logged in user's appointments
    @GetMapping("/my")
    public ResponseEntity<?> getMyAppointments(Authentication auth) {
        String email = auth.getName();
        List<Appointment> list = appointmentRepo.findByStudentEmailOrderByBookedAtDesc(email);
        return ResponseEntity.ok(list);
    }

    private String str(Map<String, Object> body, String key) {
        Object val = body.get(key);
        return val != null ? val.toString() : null;
    }
}
