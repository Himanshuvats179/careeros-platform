# ============================================================
# CareerOS Config Server — Production Dockerfile
# ============================================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml /build/pom.xml
COPY common/pom.xml /build/common/pom.xml
COPY config-server/pom.xml /build/config-server/pom.xml

RUN mvn -f /build/pom.xml -pl common,config-server -am \
    dependency:go-offline -B -q

COPY common/src /build/common/src
COPY config-server/src /build/config-server/src

RUN mvn -f /build/pom.xml -pl common,config-server -am \
    clean package -DskipTests -B -q

# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S careeros && adduser -S careeros -G careeros
WORKDIR /app

COPY --chown=careeros:careeros --from=builder /build/config-server/target/*.jar app.jar

USER careeros

ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8888

HEALTHCHECK --interval=20s --timeout=10s --start-period=30s --retries=5 \
  CMD wget -qO- http://localhost:8888/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
