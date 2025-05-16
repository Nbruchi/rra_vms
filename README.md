# 🚗 RRA Vehicle Management System (RRA-VMS)

A comprehensive API for managing vehicle registration, ownership, and transfers for the Rwanda Revenue Authority (RRA).

## 📑 Table of Contents
- [📋 Overview](#overview)
- [✨ Features](#features)
- [🛠️ Technologies Used](#technologies-used)
- [🚀 Getting Started](#getting-started)
  - [📋 Prerequisites](#prerequisites)
  - [⚙️ Installation](#installation)
  - [🔧 Configuration](#configuration)
- [📚 API Documentation](#api-documentation)
- [🔌 API Endpoints](#api-endpoints)
  - [🔐 Authentication](#authentication)
  - [🚙 Vehicle Management](#vehicle-management)
  - [👤 Owner Management](#owner-management)
- [🔒 Security](#security)
- [📄 License](#license)
- [📧 Contact](#contact)

## 📋 Overview

The RRA Vehicle Management System is a Spring Boot application designed to manage vehicle registration, ownership, and transfers. It provides a secure API for administrators to register vehicles, manage owners, and track vehicle ownership history.

## ✨ Features

- 🔐 User authentication and authorization with JWT
- 🚗 Vehicle registration and management
- 👤 Vehicle owner management
- 🔄 Vehicle ownership transfer
- 📜 Vehicle ownership history tracking
- 🔢 Plate number registration and management
- 🔍 Search functionality for vehicles and owners

## 🛠️ Technologies Used

- ☕ Java 21
- 🍃 Spring Boot 3.4.5
- 🔐 Spring Security with JWT Authentication
- 🗄️ Spring Data JPA
- 🐘 PostgreSQL Database
- 📝 Swagger/OpenAPI for API Documentation
- 🔧 Maven for dependency management
- 🧰 Lombok for reducing boilerplate code
- 🔄 ModelMapper for object mapping

## 🚀 Getting Started

### 📋 Prerequisites

- ☕ Java 21 or higher
- 🐘 PostgreSQL 12 or higher
- 🔧 Maven 3.6 or higher

### ⚙️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/rra_vms.git
   cd rra_vms
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### 🔧 Configuration

The application can be configured through the `application.properties` file:

```properties
# Database configurations
spring.datasource.url=jdbc:postgresql://localhost:5432/rra_vms
spring.datasource.username=postgres
spring.datasource.password=your_password

# JWT configurations
jwt.secret.key=your_secret_key
jwt.expiration=36000000
```

## 📚 API Documentation

Once the application is running, you can access the Swagger UI documentation at:

```
http://localhost:8080/swagger-ui.html
```

## 🔌 API Endpoints

### 🔐 Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate a user and get JWT token
- `GET /api/auth/users` - Get all users (admin only)

### 🚙 Vehicle Management

- `POST /api/vehicles` - Register a new vehicle
- `GET /api/vehicles` - Get all vehicles with pagination
- `GET /api/vehicles/owner/{ownerId}` - Get vehicles by owner
- `GET /api/vehicles/search` - Search vehicles by chassis number, plate number, or owner's national ID
- `POST /api/vehicles/transfer` - Transfer vehicle ownership
- `GET /api/vehicles/history/{identifier}` - Get vehicle ownership history

### 👤 Owner Management

- `POST /api/owners` - Create a new owner
- `GET /api/owners/` - Get owners with vehicles
- `GET /api/owners/search` - Search owners
- `GET /api/owners/{ownerId}/plates` - Get owner's plate numbers
- `POST /api/owners/plates` - Register a plate number

## 🔒 Security

The application uses JWT (JSON Web Token) for authentication and authorization. All API endpoints, except for registration and login, require authentication. Most endpoints require ADMIN role access.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📧 Contact

👨‍💻 Bruce NKUNDABAGENZI - brucenkundabagenzi@gmail.com

🔗 Project Link: [https://github.com/yourusername/rra_vms](https://github.com/yourusername/rra_vms)
