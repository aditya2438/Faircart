# Multi-stage Dockerfile for FairCart Enterprise Backend
# Stage 1: Build & Package with Eclipse Temurin JDK 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace

# Cache maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal Distroless / Alpine JRE Runtime
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Create a non-privileged application user
RUN addgroup -S faircart && adduser -S faircart -G faircart

# Copy jar artifact from build stage
COPY --from=builder /workspace/target/faircart-backend-*.jar app.jar

# Set ownership
RUN chown -R faircart:faircart /app

USER faircart:faircart

# Configuration environment variables
ENV SPRING_PROFILES_ACTIVE=dev \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep UP || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

