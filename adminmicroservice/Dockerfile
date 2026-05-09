# # Use OpenJDK 17 as base image
# FROM eclipse-temurin:17-jdk-alpine

# # Install Maven
# RUN apk add --no-cache maven curl

# # Set working directory
# WORKDIR /app

# # Copy pom.xml first for better Docker layer caching
# COPY target/*.jar app.jar

# # Download dependencies
# # RUN mvn dependency:go-offline -B

# # Copy source code
# # COPY src ./src

# # Build the application
# RUN mvn clean package -DskipTests

# # Expose port
# EXPOSE 8080

# # Run the application
# # CMD ["java", "-jar", "target/adminmicroservice-0.0.1-SNAPSHOT.jar"]
# ENTRYPOINT ["java","-jar","app.jar"]

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the already built jar
COPY target/adminmicroservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

