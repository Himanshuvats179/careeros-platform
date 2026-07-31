# ============================================================
# CareerOS Auth Service — Multi-Stage Production Dockerfile
# ============================================================
# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy parent POM first (layer caching)
COPY pom.xml /build/pom.xml
COPY common/pom.xml /build/common/pom.xml
COPY auth-service/pom.xml /build/auth-service/pom.xml

# Download dependencies only (layer cache)
RUN mvn -f /build/pom.xml -pl common,auth-service -am \
    dependency:go-offline -B -q

# Copy source
COPY common/src /build/common/src
COPY auth-service/src /build/auth-service/src

# Build the JAR (skip tests; tests run in CI pipeline)
RUN mvn -f /build/pom.xml -pl common,auth-service -am \
    clean package -DskipTests -B -q

# ============================================================
# Stage 2: Runtime (Distroless for minimal attack surface)
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Security: non-root user
RUN addgroup -S careeros && adduser -S careeros -G careeros

WORKDIR /app

# Copy built JAR
COPY --from=builder /build/auth-service/target/*.jar app.jar
COPY --chown=careeros:careeros --from=builder /build/auth-service/target/*.jar app.jar

USER careeros

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+ExitOnOutOfMemoryError \
               -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
