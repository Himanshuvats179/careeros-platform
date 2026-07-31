# API Gateway - Architecture & System Design Document

## 1. Overall System Architecture
```mermaid
graph TD
    Client[React 19 Frontend :3000] -->|HTTPS| Gateway[API Gateway :8080]
    
    subgraph Gateway Core
        JwtFilter[JWT Authentication Filter]
        Cors[CORS WebFilter]
        RateLimiter[Redis Rate Limiter]
    end
    
    Gateway -->|lb://auth-service| Auth[Auth Service :8082]
    Gateway -->|lb://profile-service| Profile[Profile Service :8081]
    Gateway -->|lb://audit-service| Audit[Audit Service :8083]
    Gateway -->|lb://job-service| Job[Job Service :8085]
    Gateway -->|lb://notification-service| Notif[Notification Service :8084]
    Gateway -->|http://localhost:8000| AIAgent[AI Agent Service :8000]
```

## 2. Component Specifications

### A. Non-Blocking Event-Driven Core
- Built on Spring WebFlux & Project Reactor on Netty server.
- Zero thread blocking during request routing and header mutation.

### B. Security & Rate Limiting Topology
- **Token Verification**: HMAC-SHA256 JJWT signature checking.
- **Header Injection**: Propagates `X-User-Id` downstream so microservices do not need to parse raw JWT tokens repeatedly.
- **Rate Limiting**: Reactive Redis KeyResolver tracking request frequency per IP.

### C. AWS Deployment Blueprint
- **Compute**: AWS EKS Kubernetes Pods with HPA scaling.
- **Load Balancer**: AWS Application Load Balancer (ALB) routing to Gateway ingress.
- **Rate Limiter**: AWS ElastiCache for Redis cluster.
