package com.growthx.assignment.repository;

import com.growthx.assignment.bean.User;
import com.growthx.assignment.mongo.MongoClientUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private Environment environment;

    private volatile MongoClientUtils mongoClientUtils;

    @Override
    public User findByUsername(String username) {
        setMongoObject();

        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "user");
        Bson filter = Filters.eq("username", username);

        FindIterable<Document> users = collection.find(filter);
        if(users != null && users.iterator() != null && users.iterator().hasNext()) {
            Document next = users.iterator().next();
            User user = new User();
            user.setUsername(next.getString("username"));
            user.setPassword(next.getString("password"));
            user.setId(next.getString("id"));
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User save(User user) {
        setMongoObject();

        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "user");
        user.setId(UUID.randomUUID().toString());
        Document doc = new Document("username", user.getUsername())
                .append("password", user.getPassword()).append("id", user.getId());
        collection.insertOne(doc);
        return user;
    }

    private synchronized void setMongoObject() {

        if(this.mongoClientUtils == null) {
            synchronized (new Object()) {
                String url = environment.getProperty("mongo.url");
                int port = environment.getProperty("mongo.port", Integer.class);
                String userName = environment.getProperty("mongo.username");
                String password = environment.getProperty("mongo.password");

                mongoClientUtils = new MongoClientUtils(userName, password, url, port);
            }
        }
    }
}
