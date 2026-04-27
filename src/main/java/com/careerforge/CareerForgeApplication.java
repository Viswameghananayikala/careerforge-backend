package com.careerforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CareerForgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CareerForgeApplication.class, args);

        System.out.println("\n✅ CareerForge Backend running at http://localhost:8081");
        System.out.println("🔑 Admin: admin@careerforge.com / CareerForge@2025\n");
    }
}