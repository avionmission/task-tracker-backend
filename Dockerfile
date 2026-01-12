# Multi-stage build for Railway
FROM eclipse-temurin:17-jdk-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install curl for debugging
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Copy the jar from builder stage
COPY --from=builder /app/target/tasks-0.0.1-SNAPSHOT.jar app.jar

# Copy startup script
COPY start.sh start.sh
RUN chmod +x start.sh

# Expose port
EXPOSE 8080

# Run the application with startup script
ENTRYPOINT ["./start.sh"]