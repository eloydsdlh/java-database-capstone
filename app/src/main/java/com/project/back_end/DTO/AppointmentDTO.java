package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.project.back_end.models.Appointment;

public class AppointmentDTO {

    private Long id;

    private Long doctorId;

    private String doctorName;

    private Long patiendId;

    private String patientName;

    private String patientEmail;

    private String patientPhone;

    private String patientAdress;

    private LocalDateTime appointmentTime;

    private int status; //Scheduled:0", "Completed:1

    private LocalDate appointmentDate;

    private LocalTime appointmentTimeOnly;

    private LocalDateTime endTime;

    public AppointmentDTO(Appointment app) {
        this.id = app.getId();
        this.doctorId = app.getDoctor().getId();
        this.doctorName = app.getDoctor().getName();
        this.patiendId = app.getPatient().getId();
        this.patientName = app.getPatient().getName();
        this.patientEmail = app.getPatient().getEmail();
        this.patientPhone = app.getPatient().getPhone();
        this.patientAdress = app.getPatient().getAddress();
        this.appointmentTime = app.getAppointmentTime();
        this.status = app.getStatus();
        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.plusHours(1);

        
    }

    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getPatiendId() {
        return patiendId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientAdress() {
        return patientAdress;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

}
