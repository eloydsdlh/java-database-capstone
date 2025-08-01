package com.project.back_end.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Prescription;

import java.util.List;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    
    // 1. Find prescriptions associated with a specific appointment
    List<Prescription> findByAppointmentId(Long appointmentId);
}


