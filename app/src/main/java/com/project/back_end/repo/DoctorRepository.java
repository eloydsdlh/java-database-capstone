package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Doctor;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
   // 1. Find a doctor by their email address
   Doctor findByEmail(String email);

   // 2. Find doctors by partial name match
   List<Doctor> findByNameLike(String name);

   // 3. Filter doctors by partial name and exact speciality
   List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

   // 4. Find doctors by specialty, ignoring case
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
