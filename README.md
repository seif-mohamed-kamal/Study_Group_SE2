# Study Group Management System 📚

A comprehensive microservices-based platform for managing study groups, built with Spring Boot, React, and Docker.

## 🏗️ Architecture Overview

This system follows a microservices architecture with the following components:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │  Eureka Server  │
│  (Port 3000)    │────│   (Port 8085)   │────│   (Port 8761)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ├─────────────────┐
                              │                 │
                    ┌─────────────────┐ ┌─────────────────┐
                    │ Study Group     │ │   Admin         │
                    │ Service (8080)  │ │ Service (8082)  │
                    └─────────────────┘ └─────────────────┘
                              │                 │
                              └─────────────────┘
                                      │
                              ┌─────────────────┐
                              │  MySQL Database │
                              │    (Port 3306)  │
                              └─────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Node.js 18+**
- **Docker & Docker Compose** (recommended)
- **MySQL 8.0+** (if running locally)

### Option 1: Docker (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd Study_Group_SE2

# Build and start all services
docker-compose up --build
```

### Option 2: Manual Setup

#### 1. Database Setup
```bash
# Start MySQL (or use existing instance)
mysql -u root -p
CREATE DATABASE studygroup_db;
```

#### 2. Backend Services

##### Build Target Folders
```bash
# Build all microservices (creates target folders)
cd discovery-server && ./mvnw clean install
cd ../studygroup && ./mvnw clean install
cd ../adminmicroservice && ./mvnw clean install
cd ../api-gateway && ./mvnw clean install
```

##### Start Services
```bash
# Start each microservice in separate terminals

# Discovery Server (Eureka)
cd discovery-server
./mvnw spring-boot:run

# Study Group Service
cd studygroup
./mvnw spring-boot:run

# Admin Service
cd adminmicroservice
./mvnw spring-boot:run

# API Gateway
cd api-gateway
./mvnw spring-boot:run
```

#### 3. Frontend
```bash
cd frontend
npm install
npm run dev
```

## 🌐 Access Points

- **Frontend Application**: http://localhost:3000
- **API Gateway**: http://localhost:8085
- **Eureka Dashboard**: http://localhost:8761
- **Study Group Service**: http://localhost:8080
- **Admin Service**: http://localhost:8082
- **MySQL Database**: localhost:3306

## 📁 Project Structure

```
Study_Group_SE2/
├── adminmicroservice/          # Admin management service
├── api-gateway/               # API Gateway for routing
├── discovery-server/           # Eureka service registry
├── frontend/                  # React frontend application
├── studygroup/               # Core study group service
├── docker-compose.yml        # Docker orchestration
├── .gitignore               # Git ignore rules
└── README.md               # This file
```

## 🔧 Services Details

### 1. Discovery Server (Eureka)
- **Port**: 8761
- **Purpose**: Service registry and discovery
- **Technology**: Spring Boot + Eureka Server

### 2. API Gateway
- **Port**: 8085
- **Purpose**: Single entry point, routing, CORS handling
- **Technology**: Spring Boot + Spring Cloud Gateway

### 3. Study Group Service
- **Port**: 8080
- **Purpose**: Core business logic for study groups
- **Features**: 
  - User authentication & authorization
  - Group creation and management
  - Material sharing
  - Comment system
  - Join requests

### 4. Admin Microservice
- **Port**: 8082
- **Purpose**: Administrative operations
- **Features**:
  - User management
  - Group oversight
  - Creator request approval

### 5. Frontend
- **Port**: 3000
- **Technology**: React + Vite
- **Features**: Modern responsive UI for study group management

## 🗄️ Database Schema

The system uses MySQL with the following main entities:
- **Users**: Student and admin accounts
- **StudyGroups**: Group information and settings
- **GroupMembers**: Many-to-many relationship between users and groups
- **Materials**: Files and resources shared in groups
- **Comments**: Discussion threads
- **JoinRequests**: Requests to join groups

## 🔐 Authentication & Security

- **JWT-based authentication**
- **Role-based access control** (Student, Admin, Creator)
- **Secure password hashing**
- **CORS configuration**

## 🛠️ Technology Stack

### Backend
- **Spring Boot 4.0.6**
- **Spring Cloud 2025.1.1**
- **Spring Security**
- **Spring Data JPA**
- **MySQL 8.0**
- **JWT (JSON Web Tokens)**
- **Maven**

### Frontend
- **React 18**
- **Vite**
- **Axios** (HTTP client)
- **Modern CSS**

### DevOps
- **Docker & Docker Compose**
- **Git**

## 📋 API Endpoints

### Study Group Service
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/groups/getAll` - Get all groups
- `POST /api/groups/create` - Create new group
- `POST /api/groups/{id}/join` - Request to join group
- `GET /api/groups/{id}/materials` - Get group materials
- `POST /api/groups/{id}/materials` - Add material
- `GET /api/groups/{id}/comments` - Get group comments
- `POST /api/groups/{id}/comments` - Add comment

### Admin Service
- `GET /api/admin/creator/requests` - Get creator requests
- `POST /api/admin/creator/approve/{id}` - Approve creator request
- `GET /api/admin/users` - Get all users
- `POST /api/admin/users/{id}/role` - Update user role

## 🐳 Docker Commands

```bash
# Build and start all services
docker-compose up --build

# Start in background
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs

# View specific service logs
docker-compose logs studygroup

# Rebuild specific service
docker-compose up --build studygroup
```

## 🔍 Development Workflow

1. **Make changes** to source code
2. **Rebuild services** (if using Docker: `docker-compose up --build`)
3. **Test changes** through API Gateway or frontend
4. **Commit changes** to Git

## 🎨 Design Patterns Used

### Architectural Patterns
- **Microservices Architecture**: Distributed system with independent services
- **API Gateway Pattern**: Single entry point for routing and cross-cutting concerns
- **Service Discovery Pattern**: Eureka for dynamic service registration and discovery

### Behavioral Patterns
- **Aspect-Oriented Programming**: Cross-cutting concerns like logging and performance monitoring
- **Repository Pattern**: Data access abstraction through repository interfaces

## 🏗️ SOLID Principles Implementation

### Single Responsibility Principle (SRP)
- Each service handles one specific business domain
- Separate controllers for different functionalities (Auth, Groups, Admin)
- Dedicated repositories for data access

### Interface Segregation Principle (ISP)
- Specific service interfaces for different operations (IGroupService, IAdminService, etc.)
- Separate DTOs for different use cases

### Dependency Inversion Principle (DIP)
- Services depend on interfaces, not concrete implementations
- Spring dependency injection manages object creation
- Repository pattern abstracts database access

## 🧹 Clean Code Practices

### Code Organization
- **Package Structure**: Clear separation by layers (controller, service, repository, model)
- **Naming Conventions**: Descriptive class, method, and variable names
- **File Organization**: Related classes grouped in appropriate packages

### Error Handling
- **Global Exception Handler**: Centralized error processing with @RestControllerAdvice
- **Custom Exception Handling**: Specific handling for validation and enum errors
- **Consistent Error Responses**: Standardized ResponseDTO format across all services

### Security
- **JWT Authentication**: Token-based security with role-based access control
- **Input Validation**: Proper validation of all incoming data
- **Role-Based Access**: Different permissions for STUDENT, CREATOR, and ADMIN roles
