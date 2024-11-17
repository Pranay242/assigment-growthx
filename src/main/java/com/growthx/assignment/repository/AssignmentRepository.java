package com.growthx.assignment.repository;

import com.growthx.assignment.bean.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AssignmentRepository {
    Assignment save(Assignment assignment);

    List<Assignment> getAssignments(String adminId);

    boolean updateAssignmentStatus(String assignmentId, String status);
}
