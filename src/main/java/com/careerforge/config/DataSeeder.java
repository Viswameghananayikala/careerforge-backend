package com.careerforge.config;

import com.careerforge.model.Counselor;
import com.careerforge.model.User;
import com.careerforge.repository.CounselorRepository;
import com.careerforge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final CounselorRepository counselorRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public DataSeeder(UserRepository userRepo,
                      CounselorRepository counselorRepo,
                      PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.counselorRepo = counselorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCounselors();
    }

    private void seedAdmin() {
        if (userRepo.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole("admin");
            userRepo.save(admin);
            System.out.println("✅ Admin seeded: " + adminEmail);
        }
    }

    private void seedCounselors() {
        if (counselorRepo.count() == 0) {
            List<Counselor> counselors = List.of(
                makeCounselor("Dr. Kavitha Rao", "Senior Career Counselor", "Technology", "BTech / Engineering",
                    "12 years", 4.9, 340, "KR",
                    "Former Google recruiter turned career coach. Specializes in tech industry placement, coding interview prep, and helping B.Tech / CS students land their dream roles.",
                    "Mon 10AM,Wed 2PM,Fri 11AM", "badge-teal", "https://example.com/resume-kavitha.pdf"),

                makeCounselor("Prof. Rajesh Kumar", "MBA & Business Coach", "Business", "Business / Commerce",
                    "18 years", 4.8, 520, "RK",
                    "IIM alumnus and visiting faculty. Expert in MBA admissions, entrepreneurship, finance careers, and business strategy.",
                    "Tue 3PM,Thu 10AM,Sat 9AM", "badge-gold", "https://example.com/resume-rajesh.pdf"),

                makeCounselor("Ms. Ananya Pillai", "Creative Careers Mentor", "Creative Arts", "Arts / Design",
                    "9 years", 4.7, 190, "AP",
                    "Award-winning designer with experience at Ogilvy and Publicis. Guides students into design, advertising, media, and creative industries.",
                    "Mon 2PM,Wed 11AM,Fri 3PM", "badge-teal", "https://example.com/resume-ananya.pdf"),

                makeCounselor("Dr. Sanjay Mehta", "Healthcare Careers Expert", "Healthcare", "Medical / Healthcare",
                    "15 years", 4.9, 280, "SM",
                    "AIIMS physician turned academic counselor. Helps students navigate NEET, medical specializations, and allied healthcare career paths.",
                    "Tue 11AM,Thu 4PM,Sun 10AM", "badge-purple", "https://example.com/resume-sanjay.pdf"),

                makeCounselor("Ms. Deepa Nair", "STEM & Research Coach", "Technology", "BTech / Engineering",
                    "11 years", 4.8, 225, "DN",
                    "PhD from IIT Madras. Specializes in data science, AI/ML, and research careers for B.Tech / Engineering students.",
                    "Mon 4PM,Wed 9AM,Fri 2PM", "badge-teal", "https://example.com/resume-deepa.pdf"),

                makeCounselor("Mr. Vikram Singh", "Finance & CA Mentor", "Business", "Business / Commerce",
                    "14 years", 4.6, 310, "VS",
                    "Chartered Accountant and ex-Deloitte consultant. Provides guidance on CA, CFA, investment banking, and financial planning.",
                    "Tue 9AM,Thu 2PM,Sat 11AM", "badge-gold", "https://example.com/resume-vikram.pdf"),

                makeCounselor("Dr. Pradeep Varma", "BTech Career Specialist", "Technology", "BTech / Engineering",
                    "10 years", 4.8, 180, "PV",
                    "IIT Bombay alumnus and career strategist. Expert in B.Tech pathways: core engineering, IT, product management, GATE, and PSU placements.",
                    "Mon 11AM,Wed 3PM,Fri 10AM", "badge-teal", "https://example.com/resume-pradeep.pdf")
            );

            counselorRepo.saveAll(counselors);
            System.out.println("✅ 7 counselors seeded");
        }
    }

    private Counselor makeCounselor(String name, String title, String specialization,
                                    String domain, String exp, double rating, int sessions,
                                    String avatar, String bio, String slots, String badge, String resume) {
        Counselor c = new Counselor();
        c.setName(name);
        c.setTitle(title);
        c.setSpecialization(specialization);
        c.setDomain(domain);
        c.setExp(exp);
        c.setRating(rating);
        c.setSessions(sessions);
        c.setAvatar(avatar);
        c.setBio(bio);
        c.setSlots(slots);
        c.setBadge(badge);
        c.setResume(resume);
        c.setPrice("Free");
        return c;
    }
}
