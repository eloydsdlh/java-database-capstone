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
import java.util.HashMap;

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
            @PathVariable String token)
    {
        Map<String, Object> map = new HashMap<>();
        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, user);
        if (!tempMap.getBody().isEmpty()) {
            map.putAll(tempMap.getBody());
            return new ResponseEntity<>(map, tempMap.getStatusCode());
        }
        map.put("message",doctorService.getDoctorAvailability(doctorId,date));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    // 2. Get List of Doctors
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        Map<String, Object> map=new HashMap<>();
        map.put("doctors",doctorService.getDoctors());
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    // 3. Add New Doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
                
        Map<String, String> response = new HashMap<>();

        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, "admin");
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }

        int res =doctorService.saveDoctor(doctor);
        if (res==1) {
            response.put("message", "Doctor added to db");
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
        }
        else if(res==-1)
        {
            response.put("message", "Doctor already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
        }
    
        response.put("message", "Some internal error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 409 Conflict     
    }

    // 4. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // 5. Update Doctor Details
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        Map<String, String> response = new HashMap<>();

        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, "admin");
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }
        int res =doctorService.updateDoctor(doctor);
        if (res==1) {
            response.put("message", "Doctor updated");
            return ResponseEntity.status(HttpStatus.OK).body(response); // 200 OK
        }
        else if(res==-1)
        {
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 Not Found
        }
    
        response.put("message", "Some internal error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 409 Conflict      
    }

    // 6. Delete Doctor
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable long id, @PathVariable String token) {
        Map<String, String> response = new HashMap<>();
        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, "admin");
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }
        int res=doctorService.deleteDoctor(id);
        if (res==1) {
            response.put("message", "Doctor deleted successfull with id: "+id);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
        }
        else if(res==-1)
        {
            response.put("message", "Doctor not found with id: "+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("message", "Some internal error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 409 Conflict
    }

    // 7. Filter Doctors
    @GetMapping("/filter/{name}/{time}/{specialty}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String specialty)
    {
        Map<String,Object> map=new HashMap<>();
        map=service.filterDoctor(name, specialty, time);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
