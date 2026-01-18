package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private TokenService tokenService;
    
    @PostMapping
    public ResponseEntity<?> createPrescription(
            @Valid @RequestBody Prescription prescription,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        // Validate token
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized", "message", "Token is required"));
        }
        
        String jwtToken = token.substring(7);
        if (!tokenService.validateToken(jwtToken)) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid token"));
        }
        
        // Validate request body
        if (prescription.getDoctorId() == null || prescription.getPatientId() == null) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Bad Request", "message", "Doctor ID and Patient ID are required"));
        }
        
        try {
            Prescription savedPrescription = prescriptionService.savePrescription(prescription);
            return ResponseEntity.status(201)
                    .body(Map.of(
                            "success", true,
                            "data", savedPrescription,
                            "message", "Prescription created successfully"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }
}
