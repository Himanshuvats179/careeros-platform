# ============================================================
# CareerOS Job Service — Multi-Stage Production Dockerfile
# ============================================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml /build/pom.xml
COPY common/pom.xml /build/common/pom.xml
COPY job-service/pom.xml /build/job-service/pom.xml

RUN mvn -f /build/pom.xml -pl common,job-service -am \
    dependency:go-offline -B -q

COPY common/src /build/common/src
COPY job-service/src /build/job-service/src

RUN mvn -f /build/pom.xml -pl common,job-service -am \
    clean package -DskipTests -B -q

# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S careeros && adduser -S careeros -G careeros
WORKDIR /app

COPY --chown=careeros:careeros --from=builder /build/job-service/target/*.jar app.jar

USER careeros

ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+ExitOnOutOfMemoryError \
               -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8084

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8084/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
