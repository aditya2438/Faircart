# ==============================================================================
# Faircart Enterprise Multi-Stage Dockerfile
# Stage 1: Build & Package JAR with Maven
# Stage 2: Minimal Distroless / JRE 21 Runtime Container
# ==============================================================================

FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache dependencies
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package
COPY backend/src ./src
RUN mvn clean package -DskipTests -B

# ==============================================================================
# Production Runtime Stage
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Add non-root system user for security
RUN addgroup -S faircart && adduser -S faircart -G faircart

WORKDIR /opt/faircart

# Install curl for container health check
RUN apk add --no-cache curl

# Copy fat JAR from builder
COPY --from=builder /app/target/faircart-backend-*.jar app.jar
RUN chown -R faircart:faircart /opt/faircart

USER faircart

# Production JVM Performance & Memory ergonomics
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE="prod"

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
