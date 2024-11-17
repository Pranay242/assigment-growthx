package com.growthx.assignment.controller;

import com.growthx.assignment.bean.Admin;
import com.growthx.assignment.bean.Assignment;
import com.growthx.assignment.bean.User;
import com.growthx.assignment.repository.AssignmentRepository;
import com.growthx.assignment.security.JwtTokenUtil;
import com.growthx.assignment.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Admin admin) throws Exception {
        Admin user1 = adminService.findByUsername(admin.getUsername());
        if(user1 != null) {
            return ResponseEntity.internalServerError().body("Admin exists");
        }
        adminService.registerAdmin(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Admin admin) throws Exception {
        boolean present = adminService.authenticateUser(admin.getUsername(), admin.getPassword());
        if(present) {
            Map<String, String> map = new HashMap<>();
            map.put("authToken", jwtTokenUtil.generateToken(admin.getUsername(), "ADMIN"));
            return ResponseEntity.ok().body(map);
        }
        return ResponseEntity.internalServerError().body("Username/Password Wrong");
    }

    @GetMapping("/dashboard/assignments")
    public ResponseEntity getAssignments(@RequestParam String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("assignments", assignmentRepository.getAssignments(username));
        return ResponseEntity.ok().body(map);
    }

    @PutMapping("/dashboard/assignments/{id}")
    public ResponseEntity<String> updateAssignmentStatus(@PathVariable String id, @RequestBody Assignment assignment) {
        try {
            // Call service layer to update the status
            boolean updated = assignmentRepository.updateAssignmentStatus(id, assignment.getStatus());

            if (updated) {
                return ResponseEntity.ok("Assignment status updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating assignment status.");
        }
    }

    @GetMapping("/dashboard/all")
    public ResponseEntity getAllUserNames() {
        Map<String, Object> map = new HashMap<>();
        map.put("admins", adminService.getAllAdminUserNames());
        return ResponseEntity.ok().body(map);
    }
}
