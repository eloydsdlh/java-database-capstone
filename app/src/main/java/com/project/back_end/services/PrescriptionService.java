package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    // Constructor Injection
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Guarda una nueva receta en la base de datos.
     * Verifica que no exista una receta para la misma cita.
     */
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> response = new HashMap<>();
        try {
            List<Prescription> exists = prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());
            if (exists != null) {
                response.put("message", "Prescription already exists for this appointment");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            prescriptionRepository.save(prescription);
            response.put("message", "Prescription saved");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Aquí podrías usar un logger real
            response.put("message", "Error saving prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la receta asociada a un appointmentId específico.
     */
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Prescription> prescription = prescriptionRepository.findByAppointmentId(appointmentId);
            if (prescription == null) {
                response.put("message", "No prescription found for this appointment");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("prescription", prescription);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Aquí también puedes usar un logger real
            response.put("message", "Error fetching prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
