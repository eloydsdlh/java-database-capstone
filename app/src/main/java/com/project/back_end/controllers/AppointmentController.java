package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.AppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppService service;

    // 1. Get Appointments
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String patientName,
            @PathVariable String token) {

        var validation = service.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        Map<String, Object> appointments = appointmentService.getAppointment(patientName, date, token);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    // 2. Book Appointment
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        var validation = service.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        int validationMessage = service.validateAppointment(appointment);
        if (validationMessage != 1) {
            return new ResponseEntity<>(Map.of("message", "Appointment not available"), HttpStatus.BAD_REQUEST);
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            return new ResponseEntity<>(Map.of("message", "Appointment booked successfully"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Map.of("message", "Failed to book appointment"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. Update Appointment
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        var validation = service.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return appointmentService.updateAppointment(appointment);
    }

    // 4. Cancel Appointment
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable long id,
            @PathVariable String token) {

        var validation = service.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return appointmentService.cancelAppointment(id, token);
    }
}
