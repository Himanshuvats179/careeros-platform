-- ============================================================
-- Flyway Migration V2: Seed Realistic Demo Job Postings
-- ============================================================

INSERT INTO job_postings (
    id, company_name, title, description, responsibilities, requirements, min_salary, max_salary, employment_type, experience_level, industry, location, is_remote, is_hybrid, status
) VALUES 
(
    'job-101', 'TechCorp Solutions', 'Senior Java Spring Boot Engineer',
    'Join our high-throughput backend architecture team building distributed microservices using Java 21, Spring Boot 3, Kafka, and PostgreSQL.',
    'Design event-driven microservices, optimize SQL queries, build resilient REST APIs, mentor junior developers.',
    '5+ years experience in Java 17+, Spring Boot, Kafka, PostgreSQL, Docker, Redis.',
    130000.00, 165000.00, 'FULL_TIME', 'SENIOR', 'Software Engineering', 'Bangalore, India', TRUE, FALSE, 'OPEN'
),
(
    'job-102', 'CloudScale Systems', 'Lead Staff Software Architect',
    'Lead platform architecture transition towards reactive microservices, Kubernetes container orchestration, and AI-assisted workflows.',
    'Define microservice boundaries, establish CI/CD practices, oversee security & telemetry pipelines.',
    '8+ years in distributed systems, Spring Cloud, AWS, Kubernetes, OpenTelemetry.',
    170000.00, 220000.00, 'FULL_TIME', 'LEAD', 'Cloud Platform', 'San Francisco, CA', FALSE, TRUE, 'OPEN'
),
(
    'job-103', 'AI Next Labs', 'AI Systems & Backend Platform Engineer',
    'Build high-performance FastAPI microservices integrating RAG vector search (ChromaDB), LangChain multi-agent workflows, and LLM inference endpoints.',
    'Develop 2-stage RAG pipelines, optimize CrossEncoder reranking models, maintain Kafka event telemetry.',
    '4+ years in Python 3.12, FastAPI, LangChain, PyTorch, Vector Databases, Docker.',
    140000.00, 185000.00, 'FULL_TIME', 'SENIOR', 'Artificial Intelligence', 'Remote', TRUE, FALSE, 'OPEN'
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO job_required_skills (job_id, skill_name) VALUES 
('job-101', 'Java 21'), ('job-101', 'Spring Boot 3'), ('job-101', 'Kafka'), ('job-101', 'PostgreSQL'), ('job-101', 'Redis'),
('job-102', 'Java 21'), ('job-102', 'Spring Cloud'), ('job-102', 'Kubernetes'), ('job-102', 'AWS'), ('job-102', 'Docker'),
('job-103', 'Python'), ('job-103', 'FastAPI'), ('job-103', 'ChromaDB'), ('job-103', 'LangChain'), ('job-103', 'PyTorch')
ON CONFLICT DO NOTHING;
