package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Derived query method to find patient by email
    Optional<Patient> findByEmail(String email);
    
    // Custom query to find patient by email or phone number
    @Query("SELECT p FROM Patient p WHERE p.email = :identifier OR p.phone = :identifier")
    Optional<Patient> findByEmailOrPhone(@Param("identifier") String identifier);
}
