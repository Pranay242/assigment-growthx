package com.growthx.assignment.repository;

import com.growthx.assignment.bean.Assignment;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class AssignmentRepositoryImpl implements AssignmentRepository {

    private volatile MongoClientUtils mongoClientUtils;
    @Autowired
    private Environment environment;

    public Assignment save(Assignment assignment) {
        setMongoObject();

        // Get the collection for assignments
        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "assignments");

        // Set the assignment ID and createdAt timestamp
        assignment.setId(UUID.randomUUID().toString());
        assignment.setCreatedAt(new Date());
        assignment.setStatus("Pending");

        // Create a MongoDB document from the assignment
        Document doc = new Document("userId", assignment.getUserId())
                .append("task", assignment.getTask())
                .append("admin", assignment.getAdmin())
                .append("createdAt", assignment.getCreatedAt())
                .append("status", "Pending").append("id", assignment.getId());  // Initial status set to "Pending"

        // Insert the document into MongoDB collection
        collection.insertOne(doc);

        // Return the assignment object with the generated ID
        return assignment;
    }

    @Override
    public List<Assignment> getAssignments(String adminId) {
        setMongoObject();

        List<Assignment> assignments = new ArrayList<>();

        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "assignments");
        Bson filter = Filters.eq("admin", adminId);

        FindIterable<Document> users = collection.find(filter);
        MongoCursor<Document> iterator = users.iterator();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            Assignment assignment = new Assignment();
            assignment.setTask(next.getString("task"));
            assignment.setId(next.getString("id"));
            assignment.setStatus(next.getString("status"));
            assignment.setUserId(next.getString("userId"));
            assignment.setCreatedAt(next.getDate("createdAt"));
            assignment.setAdmin(next.getString("admin"));

            assignments.add(assignment);
        }
        return assignments;
    }

    @Override
    public boolean updateAssignmentStatus(String assignmentId, String status) {
        setMongoObject();

        // Get the MongoDB collection for assignments
        MongoCollection<Document> collection = mongoClientUtils.getCollection(environment.getProperty("mongo.db"), "assignments");

        // Find the assignment by ID and update its status
        Document updateDoc = new Document("$set", new Document("status", status));
        com.mongodb.client.result.UpdateResult result = collection.updateOne(Filters.eq("id", assignmentId), updateDoc);

        // Return true if the update was successful, otherwise false
        return result.getModifiedCount() > 0;
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
