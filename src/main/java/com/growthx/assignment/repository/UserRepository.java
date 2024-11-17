package com.growthx.assignment.repository;

import com.growthx.assignment.bean.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository {
    User findByUsername(String username);

    User save(User user);
}
