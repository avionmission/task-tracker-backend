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

# Expose port
EXPOSE 8080

# Run the application directly with environment variable debugging
CMD echo "=== Environment Debug ===" && \
    echo "PORT: $PORT" && \
    echo "DATABASE_URL set: $([ -n "$DATABASE_URL" ] && echo "YES" || echo "NO")" && \
    echo "PGHOST: $PGHOST" && \
    echo "PGPORT: $PGPORT" && \
    echo "PGDATABASE: $PGDATABASE" && \
    echo "PGUSER: $PGUSER" && \
    echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE" && \
    echo "JWT_SECRET set: $([ -n "$JWT_SECRET" ] && echo "YES" || echo "NO")" && \
    echo "=========================" && \
    java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-railway} \
         -Dserver.port=${PORT:-8080} \
         -Djava.security.egd=file:/dev/./urandom \
         -jar app.jar