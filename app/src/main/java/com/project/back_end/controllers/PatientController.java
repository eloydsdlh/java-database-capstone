package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.AppService;
import com.project.back_end.services.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppService service;

    // 1. Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        Map<String, Object> map = new HashMap<>();
        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, "patient");
        if (!tempMap.getBody().isEmpty()) {
            map.putAll(tempMap.getBody());
            return new ResponseEntity<>(map, tempMap.getStatusCode());
        }
        return patientService.getPatientDetails(token);
    }

    // 2. Create a New Patient
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        Map<String,String> map=new HashMap<>();
        if(service.validatePatient(patient))
        {
            int res=patientService.createPatient(patient);
            if(res==1)
            {
                map.put("message","Signup successfull");
                return ResponseEntity.status(HttpStatus.CREATED).body(map); // 201 Created
            }
            if(res==0)
            {
                map.put("message","Internal server error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map); // 201 Created
            }
        }
        map.put("message", "Patient with email id or phone no already exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map); 
    }


    // 3. Patient Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    // 4. Get Patient Appointments
    @GetMapping("/{id}/{token}")
    public ResponseEntity<?> getPatientAppointment(@PathVariable long id, @PathVariable String token) {
        Map<String, Object> map = new HashMap<>();
        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, "user");
        if (!tempMap.getBody().isEmpty()) {
            map.putAll(tempMap.getBody());
            return new ResponseEntity<>(map, tempMap.getStatusCode());
        }

        return patientService.getPatientAppointment(id,token);
    }

    // 5. Filter Patient Appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token)
    {
        Map<String, Object> map = new HashMap<>();
        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, "patient");
        if (!tempMap.getBody().isEmpty()) {
            map.putAll(tempMap.getBody());
            return new ResponseEntity<>(map, tempMap.getStatusCode());
        }
        return service.filterPatient(condition,name,token);
    }
}
