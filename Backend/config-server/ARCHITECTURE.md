# Config Server - Architecture & System Design Document

## 1. Overall System Architecture
```mermaid
graph TD
    GitRepo[(Git Repository / Local Config)] <--> ConfigServer[Config Server :8888]
    
    Auth[Auth Service :8082] -->|Fetch Config| ConfigServer
    Profile[Profile Service :8081] -->|Fetch Config| ConfigServer
    Audit[Audit Service :8083] -->|Fetch Config| ConfigServer
    Job[Job Service :8085] -->|Fetch Config| ConfigServer
    Notif[Notification Service :8084] -->|Fetch Config| ConfigServer
```

## 2. Component Specifications

### A. Configuration Storage Topology
- **Profile**: Native file system search locations (`classpath:/config`).
- **Endpoints**: `GET /{application}/{profile}` (e.g. `http://localhost:8888/auth-service/default`).

### B. AWS Deployment Blueprint
- **Compute**: AWS EKS Kubernetes Pods.
- **Backend Storage**: AWS CodeCommit / GitHub private repository or AWS Systems Manager (SSM).
- **Bus Refresh**: AWS MQ / RabbitMQ spring-cloud-bus broadcast.
