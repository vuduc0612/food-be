# Multi-stage Dockerfile for Spring Boot (Java 21)

# ======================
# 1) Build stage
# ======================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Copy only pom.xml first for better layer caching
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -B -DskipTests dependency:go-offline

# Copy the rest of the source
COPY src ./src

# Build application
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -B clean package -DskipTests


# ======================
# 2) Runtime stage
# ======================
FROM eclipse-temurin:21-jre

ENV TZ=Asia/Ho_Chi_Minh \
    JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod

WORKDIR /app

# Copy fat jar from builder
COPY --from=builder /workspace/target/*.jar /app/app.jar

# Expose the application port (matches server.port in application.yml)
EXPOSE 8118

# Healthcheck (optional; adjust endpoint if needed)
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 CMD wget -qO- http://localhost:8118/actuator/health || exit 1

# Run
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


