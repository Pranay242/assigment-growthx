package com.growthx.assignment.controller;

import com.growthx.assignment.bean.Assignment;
import com.growthx.assignment.bean.User;
import com.growthx.assignment.repository.AssignmentRepository;
import com.growthx.assignment.security.JwtTokenUtil;
import com.growthx.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) throws Exception {
        User user1 = userService.findByUsername(user.getUsername());
        if(user1 != null) {
            return ResponseEntity.internalServerError().body("User exists");
        }
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) throws Exception {
        boolean present = userService.authenticateUser(user.getUsername(), user.getPassword());
        if(present) {
            Map<String, String> map = new HashMap<>();
            map.put("authToken", jwtTokenUtil.generateToken(user.getUsername(), "USER"));
            return ResponseEntity.ok().body(map);
        }
        return ResponseEntity.internalServerError().body("Username/Password Wrong");
    }

    @PostMapping("/dashboard/upload")
    public ResponseEntity uploadAssignment(@RequestBody Assignment assignment) {
        return ResponseEntity.ok().body(assignmentRepository.save(assignment));
    }
}
