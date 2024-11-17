package com.growthx.assignment.repository;

import com.growthx.assignment.bean.Admin;
import com.growthx.assignment.bean.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository{
    Admin findByUsername(String username);

    List<Admin> findAll();

    Admin save(Admin user);
}
