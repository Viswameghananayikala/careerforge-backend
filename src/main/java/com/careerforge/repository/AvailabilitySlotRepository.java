package com.careerforge.repository;

import com.careerforge.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByCounselorId(Long counselorId);

    AvailabilitySlot findByCounselorIdAndDateAndTime(Long counselorId, String date, String time);
}