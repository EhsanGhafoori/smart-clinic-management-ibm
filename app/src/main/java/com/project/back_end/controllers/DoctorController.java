package com.project.back_end.controllers;

import com.project.back_end.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;
    
    @GetMapping("/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String time,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        // Validate token
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized", "message", "Token is required"));
        }
        
        String jwtToken = token.substring(7);
        if (!doctorService.validateToken(jwtToken)) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid token"));
        }
        
        try {
            List<Map<String, Object>> availableDoctors = doctorService.getAvailableDoctors(specialty, time);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", availableDoctors,
                    "message", "Doctors retrieved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllDoctors(
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        // Validate token
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized", "message", "Token is required"));
        }
        
        try {
            List<Map<String, Object>> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", doctors,
                    "message", "Doctors retrieved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }
}
