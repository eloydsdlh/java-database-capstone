

package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.project.back_end.services.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class DashboardController {

    @Autowired
    private Service service;

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        String result = service.validateToken(token, "admin");
        if (result.equals("valid")) {
            return "admin/adminDashboard"; //  Forward to admin dashboard view
        } else {
            return "redirect:/"; // Redirect to root (e.g., login)
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        String result = service.validateToken(token, "doctor");
        if (result.equals("valid")) {
            return "admin/adminDashboard"; //  Forward to admin dashboard view
        } else {
            return "redirect:/"; // Redirect to root (e.g., login)
        }
    }
}
