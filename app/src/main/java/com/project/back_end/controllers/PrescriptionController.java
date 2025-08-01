package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppService;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppService service;

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @RequestBody Prescription prescription,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "doctor");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return validationResponse;
        }

        try {
            appointmentService.changeStatus(prescription.getAppointmentId(), 1);

            prescriptionService.savePrescription(prescription);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Prescription saved successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error while saving prescription"));
        }
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "doctor");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return validationResponse;
        }

        // Llamamos al método del service que devuelve ResponseEntity<Map<String, Object>>
        return prescriptionService.getPrescription(appointmentId);
    }

}
