package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.AppService;
import com.project.back_end.services.DoctorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppService service;

    // 1. Get Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation = service.validateToken(token, user);
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        List<String> available = doctorService.getDoctorAvailability(doctorId, date);
        return ResponseEntity.ok(Map.of("availableSlots", String.join(", ", available)));
    }

    // 2. Get List of Doctors
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        return ResponseEntity.ok(Map.of("doctors", doctorService.getDoctors()));
    }

    // 3. Add New Doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        int result = doctorService.saveDoctor(doctor);
        return switch (result) {
            case 1 -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Doctor added to db"));
            case -1 -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Doctor already exists"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Some internal error occurred"));
        };
    }

    // 4. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // 5. Update Doctor Details
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        int result = doctorService.updateDoctor(doctor);
        return switch (result) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Doctor updated"));
            case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Doctor not found"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Some internal error occurred"));
        };
    }

    // 6. Delete Doctor
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable long id, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) return validation;

        int result = doctorService.deleteDoctor(id);
        return switch (result) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
            case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Doctor not found with id"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Some internal error occurred"));
        };
    }

    // 7. Filter Doctors
    @GetMapping("/filter/{name}/{time}/{specialty}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String specialty) {

        return ResponseEntity.ok(service.filterDoctor(name, specialty, time));
    }
}
