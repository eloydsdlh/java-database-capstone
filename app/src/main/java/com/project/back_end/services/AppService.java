package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.*;
import com.project.back_end.repo.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
@Service
public class AppService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    // Constructor injection
    public AppService(TokenService tokenService,
                      AdminRepository adminRepository,
                      DoctorRepository doctorRepository,
                      PatientRepository patientRepository,
                      DoctorService doctorService,
                      PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!tokenService.validateToken(token, user)) {
                response.put("message", "Invalid or expired token");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            response.put("message", "Token valid");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error validating token");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
            if (admin != null) {
                if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                    String token = tokenService.generateToken(admin.getUsername());
                    response.put("token", token);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "Incorrect password");
                    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
                }
            } else {
                response.put("message", "Admin not found");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error during admin validation");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        if ((name == null || name.isEmpty()) &&
            (specialty == null || specialty.isEmpty()) &&
            (time == null || time.isEmpty())) {
            // Si no hay filtros, devuelve todos los doctores
            List<Doctor> allDoctors = doctorRepository.findAll();
            return Map.of("doctors", allDoctors);
        }
        // Usamos el método de doctorService para filtrar por nombre, especialidad y tiempo
        return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
    }

    public int validateAppointment(Appointment appointment) {
        Long doctorId = appointment.getDoctor().getId();
        LocalDate date = appointment.getAppointmentTime().toLocalDate();
        String requestedTime = appointment.getAppointmentTime().toLocalTime().toString().substring(0, 5);

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return -1; // Doctor no existe
        }

        List<String> availableSlots = doctorService.getDoctorAvailability(doctorId, date);

        if (availableSlots.contains(requestedTime)) {
            return 1; // Hora válida para la cita
        } else {
            return 0; // Hora no disponible
        }
    }

    public boolean validatePatient(Patient patient) {
        try {
            Patient existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
            return existing == null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida login del paciente y genera token si es correcto.
     */
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        try {
            Patient patient = patientRepository.findByEmail(login.getIdentifier());
            if (patient != null) {
                if (patient.getPassword().equals(login.getPassword())) {
                    String token = tokenService.generateToken(patient.getEmail());
                    response.put("token", token);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "Incorrect password");
                    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
                }
            } else {
                response.put("message", "Patient not found");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error during patient login validation");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Filtra las citas del paciente basado en condición y nombre del doctor.
     */
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = tokenService.extractIdentifier(token);
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                response.put("message", "Patient not found");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            ResponseEntity<Map<String, Object>> result;

            if ((condition == null || condition.isEmpty()) && (name == null || name.isEmpty())) {
                // Sin filtros, todas las citas
                result = patientService.getPatientAppointment(patient.getId(), token);
            } else if (condition != null && !condition.isEmpty() && (name == null || name.isEmpty())) {
                // Solo condición
                result = patientService.filterByCondition(condition, patient.getId());
            } else if ((condition == null || condition.isEmpty()) && name != null && !name.isEmpty()) {
                // Solo doctor
                result = patientService.filterByDoctor(name, patient.getId());
            } else {
                // Condición y doctor
                result = patientService.filterByDoctorAndCondition(condition, name, patient.getId());
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error filtering patient appointments");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
