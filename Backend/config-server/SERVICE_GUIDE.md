# Config Server - Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Runs a centralized Spring Cloud Config Server on port `:8888`.
2. Serves externalized configuration properties to all registered microservices dynamically.
3. Supports Git repositories and local native file system profiles (`classpath:/config`).
4. Eliminates the need to rebuild application JAR files when modifying configuration properties.
5. Encrypts sensitive credentials (database passwords, JWT secret keys, API keys).

## 2. Why This Service Exists
Centralized configuration management allows developers to change environment variables, database connections, rate limits, and feature flags in one central location without restarting or redeploying microservices across production environments.

## 3. Managed Configuration Repository
- `auth-service.yml`
- `profile-service.yml`
- `job-service.yml`
- `notification-service.yml`

## 4. Package & Folder Structure Explanation

### `com.careeros.config`
- **Why Main Application exists**: Annotated with `@EnableConfigServer` to expose HTTP GET REST endpoints (`http://localhost:8888/{service-name}/{profile}`).

## 5. End-to-End Request Flow
1. Microservice boots and reads `spring.config.import: "configserver:http://localhost:8888"`.
2. Microservice sends HTTP GET request to Config Server on port `:8888`.
3. Config Server reads target YAML configuration from native classpath or Git repository.
4. Config Server returns HTTP 200 payload containing environment properties.
5. Microservice initializes Spring Beans using injected configuration values.

## 6. Senior Interview Questions & Production Patterns
- **Q: How do you achieve dynamic property refresh without restarting microservices?**
  - *Answer*: Annotate Spring `@Beans` with `@RefreshScope` and send a `POST /actuator/refresh` request or publish a Spring Cloud Bus Webhook event via RabbitMQ/Kafka to notify all subscribed instances to reload configuration properties.

## 7. Future AI / ML / NLP & AWS Expansion Scope
- **AWS Parameter Store & Secrets Manager**: Bridge Spring Cloud Config Server to fetch secret credentials directly from AWS Secrets Manager or AWS Systems Manager (SSM) Parameter Store.
- **Dynamic Feature Flags**: Dynamically toggle AI Agent RAG model versions (e.g. switching between GPT-4o and Claude 3.5 Sonnet) via Config Server feature toggles.
