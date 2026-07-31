# CareerOS — Deployment Guide

> Complete, production-grade deployment reference for CareerOS microservices platform.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         INTERNET                                 │
└─────────────────────────────┬───────────────────────────────────┘
                              │
                    ┌─────────▼─────────┐
                    │   Nginx / ALB      │  :80/:443
                    │ (Ingress / K8s)   │
                    └─────────┬─────────┘
                              │
              ┌───────────────┴───────────────┐
              │                               │
    ┌─────────▼────────┐           ┌──────────▼────────┐
    │    Frontend       │           │    API Gateway     │
    │  React + Nginx    │           │  Spring Cloud GW   │
    │  :3001 / :80      │           │  :8443             │
    └──────────────────┘           └──────────┬─────────┘
                                              │ JWT Validation
                        ┌─────────────────────┼──────────────────────┐
                        │                     │                      │
              ┌─────────▼──────┐  ┌───────────▼────┐  ┌────────────▼─────┐
              │  Auth Service  │  │ Profile Service │  │   Job Service    │
              │  :8080         │  │  :8081          │  │   :8084          │
              └────────────────┘  └────────────────┘  └──────────────────┘
                        │                     │                      │
              ┌─────────▼──────┐  ┌───────────▼────┐  ┌────────────▼─────┐
              │ Notification   │  │  Audit Service  │  │   AI Agent       │
              │ Service :8082  │  │  :8083          │  │   FastAPI :8000  │
              └────────────────┘  └────────────────┘  └──────────────────┘

                    INFRASTRUCTURE LAYER
    ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
    │PostgreSQL│  │  Redis   │  │  Kafka   │  │RabbitMQ  │  │ChromaDB  │
    │  :5433   │  │  :6379   │  │  :9092   │  │ :5672    │  │  :8001   │
    └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘

                    OBSERVABILITY LAYER
    ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
    │Prometheus│  │ Grafana  │  │   Loki   │  │  Tempo   │
    │  :9090   │  │  :3000   │  │  :3100   │  │  :3200   │
    └──────────┘  └──────────┘  └──────────┘  └──────────┘
```

---

## 🚀 Quick Start — Local Development (Docker Compose)

### Prerequisites
- Docker Desktop ≥ 4.x
- Docker Compose ≥ 2.x
- Java 21 (for local builds without Docker)
- Node.js 20+ (for frontend)
- 8GB+ RAM recommended

### 1. Clone and Configure
```bash
git clone https://github.com/your-org/careeros.git
cd careeros

# Copy and edit environment variables
cp .env.example .env
# Edit .env with your values (especially JWT_SECRET and OPENAI_API_KEY)
```

### 2. Start Infrastructure Only (Fastest for Dev)
```bash
# From project root
docker compose up -d postgres redis kafka zookeeper rabbitmq mailpit

# Wait for health checks
docker compose ps
```

### 3. Start Everything
```bash
# Build and start all services
docker compose up -d --build

# Watch logs
docker compose logs -f

# Check status
docker compose ps
```

### 4. Access Services

| Service | URL | Credentials |
|---|---|---|
| Frontend | http://localhost:3001 | - |
| API Gateway | http://localhost:8443 | JWT token |
| Eureka Dashboard | http://localhost:8761 | - |
| Grafana | http://localhost:3000 | admin / admin |
| Kafka UI | http://localhost:8085 | - |
| RabbitMQ Dashboard | http://localhost:15672 | careeros / careeros123 |
| Mailpit (email) | http://localhost:8025 | - |
| PgAdmin | http://localhost:5050 | admin@careeros.com / admin |
| Prometheus | http://localhost:9090 | - |
| AI Agent | http://localhost:8000/docs | - |

---

## 🏭 Production Deployment — Kubernetes (AWS EKS)

### Prerequisites
- `kubectl` configured for your cluster
- `helm` 3.x installed
- AWS CLI configured
- Docker images pushed to ECR

### 1. Build and Push Images to AWS ECR
```bash
# Authenticate to ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin \
  <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Build and push (from Backend/ directory)
cd Backend
docker build -f auth-service/Dockerfile -t careeros/auth-service:1.0.0 .
docker tag careeros/auth-service:1.0.0 <account-id>.dkr.ecr.us-east-1.amazonaws.com/careeros/auth-service:1.0.0
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/careeros/auth-service:1.0.0

# Repeat for each service...
```

### 2. Deploy to Kubernetes
```bash
cd k8s/

# Apply in order
kubectl apply -f 00-namespace.yml
kubectl apply -f 01-configmap.yml
kubectl apply -f 02-secrets.yml   # Fill with real values first!

# Deploy services
kubectl apply -f services/

# Verify all pods are running
kubectl get pods -n careeros
kubectl get services -n careeros
```

### 3. Verify Deployment
```bash
# Check pod health
kubectl get pods -n careeros -w

# Check logs for a service
kubectl logs -n careeros -l app=auth-service -f

# Port-forward for local access
kubectl port-forward -n careeros svc/careeros-api-gateway 8443:8443
```

---

## 🗄️ Database Setup

Each microservice uses its own database (Database-Per-Service pattern):

| Service | Database |
|---|---|
| Auth Service | `careeros_auth` |
| Profile Service | `careeros_profile` |
| Notification Service | `careeros_notification` |
| Audit Service | `careeros_audit` |
| Job Service | `careeros_job` |

Databases are auto-created by [init.sql](Backend/docker/postgres/init.sql).
Schema migrations are managed by **Flyway** (runs on service startup).

---

## ☁️ AWS Production Architecture

```
Route 53 → ACM SSL → ALB → EKS Cluster
                              ├── Nginx Ingress Controller
                              ├── Microservice Pods (Auto-scaled)
                              └── Kubernetes Secrets (from AWS Secrets Manager)

Managed Services:
  PostgreSQL    → AWS RDS (Multi-AZ)
  Redis         → AWS ElastiCache (Cluster mode)
  Kafka         → AWS MSK (Multi-broker)
  S3            → AWS S3 (profile pics, resumes)
  ECR           → Container image registry
  CloudWatch    → Logs + Metrics (replaces Grafana in prod)
```

### Key AWS Services to Configure
1. **RDS PostgreSQL** — Multi-AZ, automated backups, encryption at rest
2. **ElastiCache Redis** — Cluster mode, TLS, auth token
3. **MSK (Kafka)** — SASL/SCRAM auth, multi-AZ brokers
4. **S3** — Versioning enabled, lifecycle policies, server-side encryption
5. **ECR** — Image scanning on push, lifecycle policies
6. **Secrets Manager** — Store all credentials (use External Secrets Operator)
7. **CloudWatch** — Log groups per service, custom metrics, alarms
8. **ALB** — WAF integration, HTTP→HTTPS redirect, sticky sessions

---

## 🤖 Machine Learning — Future Scope

The platform is pre-wired for ML:

```
Current Data Collection Points:
  ├── careeros.auth.events        → Login patterns, geographic data
  ├── careeros.profile.events     → Skills, experience, update frequency
  ├── careeros.job.events         → Application patterns, ATS scores
  └── careeros.ai.events          → Query types, resume analysis results

Planned ML Models (Phase 2):
  ├── Job-Profile Match Score     → Collaborative filtering (AWS SageMaker)
  ├── Skill Gap Predictor         → NLP on job descriptions vs profiles
  ├── Resume Quality Scorer       → Fine-tuned BERT on hiring outcomes
  ├── Interview Success Predictor → Time-series behavioral patterns
  └── Salary Estimator            → XGBoost on market data + profile
```

---

## 🔒 Security Checklist

- [x] JWT signed with HMAC-SHA256, validated at Gateway
- [x] Passwords hashed with BCrypt (strength 12)
- [x] Redis token blacklisting on logout
- [x] Non-root Docker containers
- [x] Rate limiting at API Gateway (Redis-backed)
- [x] CORS configured at Gateway level
- [ ] **TODO**: Enable HTTPS/TLS on all services
- [ ] **TODO**: Add OAuth2/OIDC (Google, GitHub)
- [ ] **TODO**: Enable Kafka SASL/SCRAM in production
- [ ] **TODO**: Rotate JWT secret using AWS Secrets Manager

---

## 📈 Monitoring & Alerting

### Grafana Dashboards (auto-provisioned)
- JVM metrics per service (heap, GC, threads)
- HTTP request rates, error rates, latency (RED metrics)
- Kafka consumer lag
- Database connection pool utilization

### Useful Commands
```bash
# View all service health endpoints
curl http://localhost:8080/actuator/health  # auth
curl http://localhost:8081/actuator/health  # profile
curl http://localhost:8082/actuator/health  # notification
curl http://localhost:8083/actuator/health  # audit
curl http://localhost:8084/actuator/health  # jobs
curl http://localhost:8443/actuator/health  # gateway

# View Prometheus metrics
curl http://localhost:8080/actuator/prometheus
```
