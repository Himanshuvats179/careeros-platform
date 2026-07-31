# Service Registry - Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Operates as the central Netflix Eureka Service Discovery server running on port `:8761`.
2. Receives heartbeats from all microservices (Auth, Profile, Audit, Job, Notification, API Gateway).
3. Maintains a live registry of healthy instances, dynamic IP addresses, and operational ports.
4. Enables client-side load balancing (`lb://<service-name>`) for Spring Cloud Gateway.
5. Evicts unhealthy microservice instances via automated health monitoring checks.

## 2. Why This Service Exists
Hardcoding IP addresses and ports in microservices leads to deployment bottlenecks and downtime during scaling operations. Service Registry dynamically tracks microservice instances as they auto-scale up or down in Kubernetes or cloud environments.

## 3. Microservice Registration Flow
- `auth-service` (`:8082`) registers as `AUTH-SERVICE`
- `profile-service` (`:8081`) registers as `PROFILE-SERVICE`
- `audit-service` (`:8083`) registers as `AUDIT-SERVICE`
- `notification-service` (`:8084`) registers as `NOTIFICATION-SERVICE`
- `job-service` (`:8085`) registers as `JOB-SERVICE`
- `api-gateway` (`:8080`) registers as `API-GATEWAY`

## 4. Package & Folder Structure Explanation

### `com.careeros.registry`
- **Why Main Application exists**: Annotated with `@EnableEurekaServer` to boot the embedded Tomcat server and render the interactive Eureka Admin Dashboard (`http://localhost:8761`).

## 5. End-to-End Request Flow
1. Microservice boots and sends HTTP `POST /eureka/apps/<APP_NAME>` payload to `:8761`.
2. Microservice sends periodic heartbeat pings every 30 seconds (`eureka.instance.lease-renewal-interval-in-seconds`).
3. API Gateway fetches instance list from `:8761` and caches local routing table.
4. If a service instance fails 3 consecutive heartbeats, Eureka evicts it from the active registry.

## 6. Senior Interview Questions & Production Patterns
- **Q: What is Eureka Self-Preservation Mode?**
  - *Answer*: If Eureka loses connection to a large percentage of microservice instances due to network partition rather than microservice failure, Self-Preservation Mode pauses instance eviction to prevent cascading failures.

## 7. Future AI / ML / NLP & AWS Expansion Scope
- **AWS Cloud Map & EKS DNS Discovery**: Transition to AWS Cloud Map or Kubernetes CoreDNS for native cloud service discovery.
- **Intelligent Auto-Scaling**: Integrate AI traffic predictors to trigger microservice pod spin-up before traffic spikes hit Eureka.
