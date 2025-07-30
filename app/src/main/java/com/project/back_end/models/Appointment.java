package com.project.back_end.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Doctor doctor;

    @ManyToOne
    @NotNull
    private Patient patient;

    @Future
    private LocalDateTime appointmentTime;

    @NotNull
    private int status; // 0 = Scheduled, 1 = Completed

    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);
    }
    

    private LocalDate getAppointmentDate () {
        return appointmentTime.toLocalDate();
    }

    private LocalTime getAppointmentTimeOnly () {
        return appointmentTime.toLocalTime();
    }


    public Appointment() {
        //Required by JPA
    }


    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime, int status) {
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.doctor = doctor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

