Banking application is a digital banking solution built with modern technologies to provide secure, fast, and seamless banking experiences. It leverages cutting-edge authentication mechanisms (JWT), third-party API integrations, and AI-driven support to enhance the user experience. Whether you’re tracking financial activities, generating bank statements, or interacting with customer support, Smart Bank offers a comprehensive and efficient solution for your banking needs.

# 🌟 Key Features

##### 1.💻 Digital Banking Service:

Convenient, secure, and accessible banking.

##### 2.🔒 Highly Secured Authentication

##### 📩 Login Alert:

Real-time notifications for login activities to keep your account secure.

#### 🛡 Latest JWT Implementation:

Cutting-edge JSON Web Token technology for seamless and secure authentication.

#### 3.✉ Email Service:
Effortless communication with external APIs for an enhanced user experience.

### 4.Performance Improvements

🗄 Efficient Database Design: Optimized database structure for faster data retrieval and improved scalability.

------------------------------------------------------------------------------------------------------------------------

# *Technologies Used*

🔹 Backend: Java (Spring Boot) – For robust and scalable API development.

🔹 Authentication: JWT (JSON Web Token) – Implementing state-of-the-art authentication and security.

🔹 Database: MySQL – Reliable data storage and management.

🔹 Third-Party Integration: APIs for email services.
------------------------------------------------------------------------------------------------------------------------

### *🚀 How to Run the Project*

Follow these steps to set up and run the project locally:

🛠 Prerequisites
Make sure you have the following installed:

☕Java (version 17 or higher)

📦Maven (version 3.8 or higher)

🗃MySQL (for database setup)

🛠Git (to clone the repository)

------------------------------------------------------------------------------------------------------------------------

# 🔧 Configure the Application

#### 1.Create a database in MySQL:

CREATE DATABASE smart_bank;

#### 2.Update the application.properties file in the src/main/resources directory with your MySQL credentials: properties

spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=<username>
spring.datasource.password=<password>

------------------------------------------------------------------------------------------------------------------------

#### ▶ Run the Application

Use Maven to build and run the project:

mvn spring-boot:run

#### 🌐 Access the Application

Open your browser and navigate to:

http://localhost:8080

------------------------------------------------------------------------------------------------------------------------

## 📧 Contact

Author: Aditya Shrivastava

GitHub: adityashrvstv25

Email: adityashrvstv25@gmail.com

------------------------------------------------------------------------------------------------------------------------

# 🛠 Dependencies

### Dependency for the project

<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.14.0</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.33</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.12.6</version>
		</dependency>

		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.12.6</version>
		</dependency>

		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.12.6</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
			<version>3.4.3</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<version>3.4.4</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>