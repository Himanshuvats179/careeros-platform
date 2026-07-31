# API Gateway - Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Serves as the single entry point (`:8080`) for all client traffic across CareerOS microservices.
2. Performs reactive JWT signature verification via custom `JwtAuthenticationFilter`.
3. Mutates downstream HTTP requests by injecting `X-User-Id` and `X-User-Email` headers.
4. Provides WebFlux reactive CORS configuration for React 19 Frontend clients.
5. Implements Redis IP-based Rate Limiting to prevent denial-of-service (DoS) attacks.

## 2. Why This Service Exists
The API Gateway shields internal microservice endpoints, centralizes security checks, simplifies client routing, and eliminates CORS complexity. Clients communicate solely with port `:8080`, while the Gateway balances loads using Eureka service discovery (`lb://auth-service`, `lb://profile-service`, etc.).

## 3. Microservice Routing Table
- `/api/v1/auth/**` -> `auth-service` (`:8082`)
- `/api/v1/profiles/**` -> `profile-service` (`:8081`)
- `/api/v1/audit/**` -> `audit-service` (`:8083`)
- `/api/v1/jobs/**` & `/api/v1/applications/**` -> `job-service` (`:8085`)
- `/api/v1/notifications/**` -> `notification-service` (`:8084`)
- `/api/v1/ai/**` -> `ai-agent-service` (`:8000`)

## 4. Package & Folder Structure Explanation

### `com.careeros.gateway.filter`
- **Why Filter exists**: Contains `JwtAuthenticationFilter` that intercepts incoming requests, validates tokens, and rejects unauthorized traffic before forwarding requests to backend services.

### `com.careeros.gateway.config`
- **Why Config exists**: Sets up `CorsConfig` (CORS policies), `RateLimiterConfig` (Redis IP rate limiting), and `OpenApiConfig` (Swagger UI aggregation).

## 5. End-to-End Request Flow
1. Client sends HTTP request (e.g. `GET /api/v1/jobs`) with header `Authorization: Bearer <JWT>`.
2. `JwtAuthenticationFilter` intercepts request on port `:8080`.
3. If public endpoint (e.g. `/api/v1/auth/login`), request passes directly.
4. For protected routes, `JwtAuthenticationFilter` verifies HMAC-SHA256 signature using shared secret.
5. Filter extracts `userId` and mutates request headers adding `X-User-Id: <UUID>`.
6. Gateway routes request via Eureka load balancer (`lb://job-service`) to Job Service (`:8085`).

## 6. Senior Interview Questions & Production Patterns
- **Q: Why use Spring Cloud Gateway (WebFlux / Netty) instead of Spring MVC Zuul?**
  - *Answer*: Spring Cloud Gateway is built on non-blocking Netty event loops, allowing thousands of concurrent requests per Gateway instance with significantly lower thread context switching overhead compared to blocking Spring MVC servlet containers.

## 7. Future AI / ML / NLP & AWS Expansion Scope
- **AWS WAF (Web Application Firewall)**: Deploy AWS WAF in front of Gateway to block SQL injection and cross-site scripting (XSS) attacks.
- **AWS API Gateway Migration**: Transition routes seamlessly to AWS API Gateway for managed serverless scaling and IAM authorization.
