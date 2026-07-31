# Auth Service - Architecture & System Design Document

## 1. Overall System Architecture
```mermaid
graph TD
    Client[React 19 Frontend] -->|REST API| Gateway[API Gateway :8080]
    Gateway -->|Eureka Routing| AuthService[Auth Service :8082]
    
    AuthService -->|User & Token Storage| AuthDB[(PostgreSQL - auth_db)]
    AuthService -->|Token Blacklist| Redis[(Redis Cache)]
    AuthService -->|Publish Audit Events| Kafka[Apache Kafka :9092]
    
    Kafka -->|Consume Events| AuditService[Audit Service :8083]
```

## 2. Component Specifications

### A. Database Schema & Flyway
- **Flyway Migration**: `V1__init_auth_schema.sql`
- **Tables**: `users`, `user_roles`, `refresh_tokens`
- **Indexes**: `idx_user_email` (UNIQUE), `idx_token_value` (UNIQUE)

### B. Security & Token Lifecycle
- **Password Encoder**: BCryptPasswordEncoder (strength 10)
- **Token Algorithm**: HMAC-SHA256 (`jjwt`)
- **Token Rotation**: Old refresh tokens deleted on new token generation.

### C. AWS Deployment Blueprint
- **Compute**: AWS EKS Kubernetes Pods with HPA scaling.
- **Database**: AWS RDS PostgreSQL Multi-AZ.
- **Cache**: AWS ElastiCache for Redis token revocation.
- **Cognito Option**: AWS Cognito User Pools integration.
