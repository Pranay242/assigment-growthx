package com.growthx.assignment.service;

import com.growthx.assignment.bean.Admin;
import com.growthx.assignment.bean.Assignment;
import com.growthx.assignment.bean.User;
import com.growthx.assignment.repository.AdminRepository;
import com.growthx.assignment.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Admin registerAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public boolean authenticateAdmin(String username, String password) {
        Admin user = adminRepository.findByUsername(username);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    public void acceptAssignment(String assignmentId) {
        /*Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setStatus("Accepted");*/
        //assignmentRepository.save(assignment);
    }

    public void rejectAssignment(String assignmentId) {
        /*Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setStatus("Rejected");*/
        //assignmentRepository.save(assignment);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public boolean authenticateUser(String username, String password) {
        Admin user = adminRepository.findByUsername(username);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    public List<String> getAllAdminUserNames() {
        return adminRepository.findAll().stream().map(Admin::getUsername).collect(Collectors.toUnmodifiableList());
    }

}
