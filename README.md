# 🏦 Banking Application

A secure, full-stack **Banking Application** built using **Spring Boot**, with robust features like user registration, authentication with **JWT**, transaction management, email notifications, and secure access control using **Spring Security**. This project follows a layered architecture and leverages **MySQL**, **JPA**, and **Lombok** for rapid development.

---

## 🚀 Tech Stack

- **Spring Boot** – Main application framework
- **Spring Data JPA** – ORM for database access
- **Spring Security** – Secure login and API protection
- **JWT (JSON Web Tokens)** – Stateless authentication
- **Spring Starter Mail** – For sending email notifications (e.g., transaction alerts)
- **Lombok** – Reduces boilerplate code (getters, setters, constructors)
- **MySQL** – Relational database

---

## 📦 Features

- 🔐 **User Registration and Login**
- 🔑 **JWT-based Authentication**
- 🧾 **Account Management**
- 💸 **Fund Transfer between Accounts**
- 📊 **Transaction History**
- 📧 **Email Notification** on key activities
- 🛡️ **Secure API Endpoints**
- 📁 **DTOs and Entity Separation**
- ✅ **Validation and Exception Handling**

---

## 🗂️ Project Structure

```
src/
├── config/              # Security and JWT configuration
├── controller/          # REST API Controllers
├── dto/                 # Data Transfer Objects
├── entity/              # JPA Entities (User, Transaction)
├── enums/               # Enums (TransactionType)
├── repo/                # Spring Data JPA Repositories
├── service/             # Service Interfaces and Implementations
├── utils/               # JWT and Utility classes
└── main/                # Main Application Entry
```

---

## 🛠️ Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL Server

### Clone the repository

```bash
git clone https://github.com/shriaditya2503/BankingApplication.git
cd BankingApplication
```

### Create Database
```sql
CREATE DATABASE banking_db
```

### Configure `application.properties`

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=root
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Mail Configuration
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## ▶️ Running the Application

```bash
mvn spring-boot:run
```

The application will start on:  
📍 `http://localhost:8080`

---

## 🔒 Authentication

- Users log in using `/user/login` and receive a JWT token.
- This token must be included in headers for secured endpoints:

```http
Authorization: Bearer <token>
```

---

## 📬 Email Service

- Email is sent to customers for:
  - Registration success
  - Password resets
  - Updating user details 
  - Transaction confirmations

---

## 📮 API Endpoints (Sample)

| Endpoint                         | Method | Access         |
|----------------------------------|--------|----------------|
| `/user/register`                 | POST   | Public         |
| `/user/login`                    | POST   | Public         |
| `/user/fetch-user-detail`        | GET    | Authenticated  |
| `/user/update-user-details`      | POST   | Authenticated  |
| `/user/transfer-fund`            | POST   | Authenticated  |
| `/transactions/transaction-list` | POST   | Authenticated  |

---

## ✍️ Author

- **Aditya Shrivastava**
- **Email** – [shriaditya2503@gmail.com](mailto:shriaditya2503@gmail.com)
- **GitHub** – [shriaditya2503](https://github.com/shriaditya2503)
