# Service Registry - Architecture & System Design Document

## 1. Overall System Architecture
```mermaid
graph TD
    Dashboard[Eureka Dashboard :8761] <--> Registry[Eureka Service Registry]
    
    Gateway[API Gateway :8080] -->|Fetch Registry Table| Registry
    
    Auth[Auth Service :8082] -->|Register & Heartbeat| Registry
    Profile[Profile Service :8081] -->|Register & Heartbeat| Registry
    Audit[Audit Service :8083] -->|Register & Heartbeat| Registry
    Job[Job Service :8085] -->|Register & Heartbeat| Registry
    Notif[Notification Service :8084] -->|Register & Heartbeat| Registry
```

## 2. Component Specifications

### A. Core Discovery Topology
- **Standalone Server**: Runs on port `:8761` with `register-with-eureka: false` and `fetch-registry: false`.
- **Heartbeat Schedule**: 30s lease renewal interval with 90s expiration threshold.
- **Eviction Timer**: 60s background eviction scanner.

### B. AWS Deployment Blueprint
- **Compute**: AWS EKS Pods or AWS ECS Tasks.
- **High Availability**: Multi-AZ Eureka Server peer replication.
- **Cloud Native Alternative**: AWS Cloud Map private DNS namespace.
