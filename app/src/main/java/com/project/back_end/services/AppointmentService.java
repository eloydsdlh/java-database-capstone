package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppService service;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              AppService service,
                              TokenService tokenService,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.service = service;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // 1. Book Appointment
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 2. Update Appointment
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointment.getId());
        if (optionalAppointment.isEmpty()) {
            response.put("message", "No appointment available with id: " + appointment.getId());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        int validationMessage = service.validateAppointment(appointment);
        if (validationMessage == -1) {
            response.put("message", "Doctor not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            appointmentRepository.save(appointment);
            response.put("message", "Appointment updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Failed to update appointment");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. Cancel Appointment
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            response.put("message", "Appointment not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!tokenService.validateToken(token, "patient")) {
            response.put("message", "Invalid or expired token");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String email = tokenService.extractIdentifier(token);
        if (email == null) {
            response.put("message", "Failed to extract user from token");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Patient patient = patientRepository.findByEmail(email);
        if (patient == null) {
            response.put("message", "Patient not found");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Appointment appointment = optionalAppointment.get();

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            response.put("message", "Unauthorized: You can only cancel your own appointments");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        try {
            appointmentRepository.delete(appointment);
            response.put("message", "Appointment cancelled successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error cancelling appointment");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4. Get Appointments
    @Transactional
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> response = new HashMap<>();

        if (!tokenService.validateToken(token, "doctor")) {
            response.put("error", "Invalid or expired token");
            return response;
        }

        String email = tokenService.extractIdentifier(token);
        if (email == null) {
            response.put("error", "Failed to extract doctor identity from token");
            return response;
        }

        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null) {
            response.put("error", "Doctor not found");
            return response;
        }

        Long doctorId = doctor.getId();

        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            List<Appointment> appointments;
            if (pname != null && !pname.trim().isEmpty()) {
                appointments = appointmentRepository
                        .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                                doctorId, pname, startOfDay, endOfDay
                        );
            } else {
                appointments = appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);
            }
            List<AppointmentDTO> appointmentsDto = appointments.stream()
                .map(app -> new AppointmentDTO(app)).collect(Collectors.toList());
            response.put("appointments", appointmentsDto);
            return response;

        } catch (Exception e) {
            response.put("error", "Error retrieving appointments");
            return response;
        }
    }

    // 5. Change Status
    @Transactional
    public void changeStatus(Long appointmentId) {
        appointmentRepository.updateStatus(1, appointmentId);
    }
}
