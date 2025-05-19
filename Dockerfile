# Use a base image with Java
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy your jar into the container
COPY target/library-0.0.1-SNAPSHOT.jar app.jar
# Expose port (same as your Spring Boot server.port)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
