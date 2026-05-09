# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .
# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven curl

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY . .

# Download dependencies
# RUN mvn dependency:go-offline -B

# Copy source code
# COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8761

# Run the application
CMD ["java", "-jar", "target/discovery-server-0.0.1-SNAPSHOT.jar"]

