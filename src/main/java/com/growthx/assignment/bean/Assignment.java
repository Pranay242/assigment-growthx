package com.growthx.assignment.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "assignments")
@Data
public class Assignment {

    @Id
    private String id;

    private String userId;
    private String task;
    private String admin;
    private Date createdAt;
    private String status;  // Pending, Accepted, Rejected
}
