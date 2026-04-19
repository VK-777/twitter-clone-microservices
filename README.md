# Microservices-Based Twitter Clone

A scalable full stack Twitter-like application built using **Spring Boot microservices architecture**, **Angular frontend**, **JWT authentication**, and an **API Gateway**.

---

## 🚀 Key Highlights
- Implemented JWT-based authentication for secure APIs  
- Designed API Gateway for centralized routing  
- Built scalable microservices architecture

---

## 🖥️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Microservices Architecture
- REST APIs

### Frontend
- Angular
- TypeScript
- HTML, CSS, Bootstrap

### Security & Architecture
- JWT Authentication
- API Gateway

### Database
- MySQL

### Tools
- Git
- Maven
- Jenkins (CI/CD)

---

## 🧠 System Architecture

The application is designed using a **microservices architecture**, where each service is independently deployable and responsible for a specific functionality.

### Services:
- **user-service** → Handles user data and authentication
- **tweet-service** → Manages tweet creation and retrieval
- **followers-service** → Handles follow/unfollow functionality
- **search-service** → Enables searching users/tweets
- **media-service** → Handles media uploads
- **api-gateway** → Central entry point for all client requests

---

## 🔄 Request Flow (Example: Create Tweet)

1. User sends request from Angular frontend  
2. Request goes to **API Gateway**  
3. Gateway validates and routes request to **tweet-service**  
4. **JWT token is verified** for authentication  
5. Tweet is processed and stored in database  
6. Response is returned via gateway to frontend  

---

## 🔐 Authentication

- Implemented using **JWT (JSON Web Tokens)**
- Stateless authentication mechanism
- Token is validated on each request
- Ensures secure communication between client and services

---

## ⚙️ Why Microservices?

- Independent deployment of services  
- Better scalability (each service scales independently)  
- Fault isolation (failure in one service doesn’t break entire system)  
- Improved maintainability and modularity  

---

## 📂 Project Structure

```bash
twitter-clone-microservices/
│
├── backend/
│   ├── user-service/
│   ├── tweet-service/
│   ├── followers-service/
│   ├── search-service/
│   ├── media-service/
│   └── api-gateway/
│
├── frontend/
│   └── Angular application
│
├── README.md
└── .gitignore
```
---

## 🗄️ Database Setup

Before running the application, ensure the required database tables are created.

- Database used: **MySQL**
- SQL scripts for table creation are available in respective service folders

### Steps:
1. Create a MySQL database
2. Run the provided SQL scripts for each service
3. Ensure database connection properties are correctly configured in application files

---

## ▶️ How to Run

### Backend
1. Navigate to each service folder inside `backend/`
2. Run using:
   ```bash
   mvn spring-boot:run
   
### Frontend
1. Navigate to `frontend/`
2. Install dependencies:
   npm install
3. Start Angular app:
   ```bash
   ng serve
5. Open browser:
   http://localhost:4200

---

## 📌 Features

- User authentication and authorization (JWT)
- Tweet creation and feed system
- Follow/unfollow users
- Search functionality
- Media handling support
- API Gateway routing
- Microservices-based architecture

---

## 🚀 Future Improvements

- Add Docker containerization
- Implement service discovery (Eureka)
- Add centralized logging and monitoring
- Improve caching (Redis)
- Rate limiting at API Gateway

---

## 👨‍💻 Author

**Vedant Kumar**  
GitHub: https://github.com/VK-777
