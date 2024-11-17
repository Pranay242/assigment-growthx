# Assignment Submission Portal

## Table of Contents
- [Objective](#objective)
- [Demo Link](#demo)
- [Database](#database)
- [Authentication](#authentication)
- [UI Page](#ui-page)
- [Tech Stack](#tech-stack)
- [API Endpoints](#api-endpoints)
- [Installation and Setup](#installation-and-setup)


## Objective
Create a backend system for an **Assignment Submission Portal** that supports **Users** and **Admins**:

- **Users** can upload assignments.
- **Admins** can:
    - See assignments tagged to them.
    - View the user's name, task, and submission date.
    - Accept or reject assignments.

## Demo
[Watch the demo](https://drive.google.com/file/d/1CpdGVJH3SppxY6E25eKpNIySvbwnF7rj/view?usp=drive_link)

## Database

### 1. **Assignments** (MongoDB)

The `Assignments` collection stores all submitted assignments. A typical assignment document might look like this:
```json
{
  "_id": {
    "$oid": "6739fba29b994e2dfd485bfd"
  },
  "userId": "Pranay",
  "task": "This is assignment 1",
  "admin": "TeacherX",
  "createdAt": {
    "$date": "2024-11-17T14:20:18.763Z"
  },
  "status": "Accepted",
  "id": "3fc075c5-cb6a-4149-80b4-e747f18a95a6"
}
```
### 2. **User** (MongoDB)

The `user` collection stores all users. A typical user document might look like this:
```json
{
  "_id": {
    "$oid": "6739fb769b994e2dfd485bfc"
  },
  "username": "Pranay",
  "password": "pranay",
  "id": "54a08c59-341d-4839-9a38-4e9431081a89"
}
```
### 3. **Admin** (MongoDB)

The `admin` collection stores all admins. A typical admin document might look like this:
```json
{
  "_id": {
    "$oid": "6739fb5b9b994e2dfd485bf9"
  },
  "username": "TeacherX",
  "password": "teacher",
  "id": "7b562feb-ab1e-4b88-bc95-35c7a7c609c6"
}
```

## Authentication
- JWT token is used to authenticate the API endpoint.
- Login, Register, Home is ignored for authentication.
- Other that these all APIs where data is displayed on the UI are authenticated using JWT token.
- Please find Authentication filter code in JwtAuthenticationFilter.java

## UI Page
- UI has home page, where you can go to either register or logIn as admin/user. 
- If you logged in as a user, you can submit your assignment to an admin.
- If you logged in as an admin, you can see all assigned assignments to you, then you Accept or Reject it.
- Validations like, user exists, admin exists, wrong username/password are handled.
- Please check out the demo video.
- 
## Tech Stack
- Springboot
- Mongo
- HTML
- JavaScript

## API Endpoints
### User Endpoints:
- `POST /user/register` : Register a new user. (No auth required)
- `POST /user/login` : User login (generates JWT token).
- `POST /user/dashboard/upload`: Upload an assignment (requires JWT).
### Admin Endpoints:
- `POST /admin/register`: Register a new admin.
- `POST /admin/login` : Admin login (generates JWT token).
- `GET /admin/dashboard/assignments` : View assignments tagged to the admin (filter by admin's username).
- `PUT /admin/dashboard/assignments/{id}` : Accept an assignment. (Pass Accepted or Rejected in the body)
- `GET /user/admins`: Fetch all admins (returns a list of users with role admin).

## Installation and Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/Pranay242/assignment
   cd ruleEngineWithAST
   #mongo.url=localhost
   #mongo.db.name=assignment_growthx
   #mongo.port=27017
   #mongo.username=
   #mongo.password=
   
   Please provide above details about your mongodb. Please create the db if not created.
   
   ## Start the server from this class com.growthx.assignment.AssignmentApplication
   ## URL to open the application - your-host-ip:8080/ (localhost:8080/)
     ```
   
