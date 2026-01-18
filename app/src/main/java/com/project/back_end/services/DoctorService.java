package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private TokenService tokenService;
    
    public List<Map<String, Object>> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return Collections.emptyList();
        }
        
        Doctor doctor = doctorOpt.get();
        List<Map<String, Object>> timeSlots = new ArrayList<>();
        
        // Parse available times from JSON string
        // This is a simplified version - in production, you'd parse JSON properly
        String availableTimes = doctor.getAvailableTimes();
        if (availableTimes != null && !availableTimes.isEmpty()) {
            // Example: Parse and return available time slots
            // For now, return a basic structure
            Map<String, Object> slot = new HashMap<>();
            slot.put("date", date.toString());
            slot.put("time", "09:00");
            slot.put("available", true);
            timeSlots.add(slot);
        }
        
        return timeSlots;
    }
    
    public Map<String, Object> validateDoctorLogin(String email, String password) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        
        if (doctorOpt.isEmpty()) {
            return Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            );
        }
        
        Doctor doctor = doctorOpt.get();
        // In production, use proper password hashing (BCrypt)
        if (!doctor.getPassword().equals(password)) {
            return Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            );
        }
        
        String token = tokenService.generateToken(doctor.getEmail());
        return Map.of(
                "success", true,
                "token", token,
                "doctor", Map.of(
                        "id", doctor.getId(),
                        "name", doctor.getName(),
                        "email", doctor.getEmail(),
                        "specialty", doctor.getSpecialty()
                ),
                "message", "Login successful"
        );
    }
    
    public List<Map<String, Object>> getAvailableDoctors(String specialty, String time) {
        List<Doctor> doctors;
        if (specialty != null && !specialty.isEmpty()) {
            doctors = doctorRepository.findBySpecialty(specialty);
        } else {
            doctors = doctorRepository.findAll();
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Doctor doctor : doctors) {
            Map<String, Object> doctorInfo = new HashMap<>();
            doctorInfo.put("id", doctor.getId());
            doctorInfo.put("name", doctor.getName());
            doctorInfo.put("email", doctor.getEmail());
            doctorInfo.put("specialty", doctor.getSpecialty());
            doctorInfo.put("phone", doctor.getPhone());
            result.add(doctorInfo);
        }
        
        return result;
    }
    
    public List<Map<String, Object>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Doctor doctor : doctors) {
            Map<String, Object> doctorInfo = new HashMap<>();
            doctorInfo.put("id", doctor.getId());
            doctorInfo.put("name", doctor.getName());
            doctorInfo.put("email", doctor.getEmail());
            doctorInfo.put("specialty", doctor.getSpecialty());
            doctorInfo.put("phone", doctor.getPhone());
            result.add(doctorInfo);
        }
        
        return result;
    }
    
    public boolean validateToken(String token) {
        return tokenService.validateToken(token);
    }
}
