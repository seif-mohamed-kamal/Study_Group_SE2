# StudyGroup Microservices - Docker Setup

## 🐳 Docker Deployment Guide

This guide explains how to deploy the StudyGroup microservices application using Docker and Docker Compose.

## 📋 Prerequisites

- Docker Desktop installed and running
- Docker Compose (included with Docker Desktop)
- At least 4GB RAM available for Docker

## 🚀 Quick Start

### 1. Build and Run All Services

```bash
# Navigate to project root
cd Project_Cloud_all

# Build and start all services
docker-compose up --build
```

### 2. Access Services

- **API Gateway**: http://localhost:8085
- **Eureka Dashboard**: http://localhost:8761
- **Study Group Service**: http://localhost:8080
- **Admin Service**: http://localhost:8081
- **MySQL Database**: localhost:3306

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │  Eureka Server  │
│  (Port 5173)    │────│   (Port 8085)   │────│   (Port 8761)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ├─────────────────┐
                              │                 │
                    ┌─────────────────┐ ┌─────────────────┐
                    │ Study Group     │ │   Admin         │
                    │ Service (8080)  │ │ Service (8081)  │
                    └─────────────────┘ └─────────────────┘
                              │                 │
                              └─────────────────┘
                                      │
                              ┌─────────────────┐
                              │  MySQL Database │
                              │    (Port 3306)  │
                              └─────────────────┘
```

## 🔧 Services Configuration

### MySQL Database
- **Image**: mysql:8.0
- **Database**: studygroup_db
- **Credentials**: root/rootpassword
- **Volume**: Persistent data storage

### Eureka Discovery Server
- **Port**: 8761
- **Purpose**: Service registry and discovery
- **Health Check**: HTTP endpoint monitoring

### API Gateway
- **Port**: 8085
- **Purpose**: Single entry point for all client requests
- **Features**: CORS, routing, load balancing

### Study Group Service
- **Port**: 8080
- **Purpose**: Core business logic for study groups
- **Database**: MySQL

### Admin Microservice
- **Port**: 8081 (mapped to container 8080)
- **Purpose**: Administrative operations
- **Database**: MySQL

## 🛠️ Docker Commands

### Build Images
```bash
# Build all services
docker-compose build

# Build specific service
docker-compose build studygroup
```

### Start Services
```bash
# Start all services in background
docker-compose up -d

# Start with logs
docker-compose up

# Start specific service
docker-compose up studygroup
```

### Stop Services
```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### View Logs
```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs studygroup

# Follow logs in real-time
docker-compose logs -f api-gateway
```

### Monitor Services
```bash
# List running containers
docker-compose ps

# Check service health
docker-compose exec studygroup curl http://localhost:8080/actuator/health
```

## 🔍 Health Checks

All services include health checks:

- **MySQL**: `mysqladmin ping`
- **Eureka**: HTTP check on port 8761
- **API Gateway**: HTTP check on port 8085
- **Study Group**: HTTP check on `/api/groups/getAll`
- **Admin Service**: HTTP check on `/api/admin/creator/requests`

## 🌐 Network Configuration

Services communicate through a dedicated Docker network:
- **Network Name**: studygroup-network
- **Driver**: bridge
- **Service Discovery**: Eureka registration

## 📦 Volumes

- **mysql_data**: Persistent MySQL data storage
- **Location**: Docker-managed volume

## 🔧 Environment Variables

### Database Configuration
```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/studygroup_db
SPRING_DATASOURCE_USERNAME: root
SPRING_DATASOURCE_PASSWORD: rootpassword
```

### Eureka Configuration
```yaml
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE: http://discovery-server:8761/eureka
```

## 🐛 Troubleshooting

### Port Conflicts
If ports are already in use:
```bash
# Check what's using the ports
netstat -ano | findstr :8085

# Kill conflicting processes
taskkill /PID <PID> /F
```

### Service Startup Issues
```bash
# Check service logs
docker-compose logs <service-name>

# Restart specific service
docker-compose restart <service-name>

# Rebuild and restart
docker-compose up --build <service-name>
```

### Database Connection Issues
```bash
# Check MySQL container
docker-compose exec mysql mysql -u root -p

# Verify database exists
SHOW DATABASES;
```

### Memory Issues
```bash
# Check Docker resource usage
docker system df
docker stats

# Clean up unused resources
docker system prune -a
```

## 🔄 Development Workflow

### 1. Make Code Changes
Edit source files in your IDE

### 2. Rebuild and Restart
```bash
# Rebuild specific service
docker-compose up --build studygroup

# Or rebuild all
docker-compose up --build
```

### 3. Test Changes
Access services through the API Gateway at http://localhost:8085

## 📊 Monitoring

### Service Status
```bash
# Check all services
docker-compose ps

# Detailed container info
docker inspect <container-name>
```

### Resource Usage
```bash
# Real-time stats
docker stats

# Disk usage
docker system df
```

## 🔒 Security Considerations

- Change default database passwords in production
- Use environment files for sensitive data
- Implement proper network segmentation
- Add authentication/authorization to services

## 🚀 Production Deployment

For production deployment:

1. **Use Docker Swarm or Kubernetes**
2. **Implement proper logging**
3. **Add monitoring (Prometheus, Grafana)**
4. **Use secrets management**
5. **Implement backup strategies**
6. **Add SSL/TLS termination**

## 📞 Support

If you encounter issues:

1. Check logs: `docker-compose logs`
2. Verify service health: `docker-compose ps`
3. Check network connectivity
4. Review resource usage: `docker stats`

---

**Note**: This Docker setup is optimized for development. For production, consider using orchestration tools like Kubernetes or Docker Swarm.
