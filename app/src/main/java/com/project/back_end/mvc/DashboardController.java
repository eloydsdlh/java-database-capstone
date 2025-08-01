package com.project.back_end.mvc;

import com.project.back_end.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private AppService service;

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        ResponseEntity<Map<String, String>> response = service.validateToken(token, "admin");

        if (response.getStatusCode().is2xxSuccessful()) {
            return "admin/adminDashboard"; // Forward to admin dashboard view
        } else {
            return "redirect:/"; // Redirect to root (e.g., login)
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        ResponseEntity<Map<String, String>> response = service.validateToken(token, "doctor");

        if (response.getStatusCode().is2xxSuccessful()) {
            return "doctor/doctorDashboard"; // Make sure to return doctor dashboard, not admin
        } else {
            return "redirect:/"; // Redirect to root (e.g., login)
        }
    }
}