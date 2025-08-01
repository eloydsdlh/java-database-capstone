package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Appointment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
   // 1. Find appointments by doctor ID within a time range
   List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

   // 2. Find appointments by doctor ID, patient name (partial match), and time range (case insensitive)
   List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
      Long doctorId,
      String patientName,
      LocalDateTime start,
      LocalDateTime end
   );

   // 3. Delete all appointments by doctor ID
   @Modifying
   @Transactional
   void deleteAllByDoctorId(Long doctorId);

   // 4. Get all appointments by patient ID
   List<Appointment> findByPatientId(Long patientId);

   // 5. Get appointments by patient ID and status, ordered by appointment time
   List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

   // 6. Filter by doctor name and patient ID
   @Query("SELECT a FROM Appointment a WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId")
   List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

   // 7. Filter by doctor name, patient ID, and appointment status
   @Query("SELECT a FROM Appointment a WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId AND a.status = :status")
   List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status);

   // 8. Update the status of an appointment
   @Modifying
   @Transactional
   @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
   void updateStatus(int status, long id);
}
