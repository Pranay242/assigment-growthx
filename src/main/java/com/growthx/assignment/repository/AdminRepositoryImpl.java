package com.growthx.assignment.repository;

import com.growthx.assignment.bean.Admin;
import com.growthx.assignment.bean.User;
import com.growthx.assignment.mongo.MongoClientUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

    @Autowired
    private Environment environment;

    private volatile MongoClientUtils mongoClientUtils;

    @Override
    public Admin findByUsername(String username) {
        setMongoObject();

        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "admin");
        Bson filter = Filters.eq("username", username);

        FindIterable<Document> users = collection.find(filter);
        if(users != null && users.iterator() != null && users.iterator().hasNext()) {
            Document next = users.iterator().next();
            Admin user = new Admin();
            user.setUsername(next.getString("username"));
            user.setPassword(next.getString("password"));
            user.setId(next.getString("id"));
            return user;
        } else {
            return null;
        }
    }

    @Override
    public List<Admin> findAll() {
        setMongoObject();

        List<Admin> admins = new ArrayList<>();

        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "admin");
        FindIterable<Document> users = collection.find();
        MongoCursor<Document> iterator = users.iterator();
        while(iterator.hasNext()) {
            Document next = iterator.next();
            Admin user = new Admin();
            user.setUsername(next.getString("username"));
            user.setPassword(next.getString("password"));
            user.setId(next.getString("id"));
            admins.add(user);
        }

        return admins;
    }

    @Override
    public Admin save(Admin user) {
        setMongoObject();

        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "admin");
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
