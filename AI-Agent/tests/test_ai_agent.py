from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "UP"

def test_analyze_resume():
    payload = {
        "user_id": "123e4567-e89b-12d3-a456-426614174000",
        "resume_text": "Experienced Java developer proficient in Spring Boot, PostgreSQL, and Kafka.",
        "target_role": "Senior Software Architect"
    }
    response = client.post("/api/v1/resume/analyze", json=payload)
    assert response.status_code == 200
    res = response.json()
    assert res["success"] is True
    assert "formatting_score" in res["data"]

def test_generate_career_roadmap():
    payload = {
        "user_id": "123e4567-e89b-12d3-a456-426614174000",
        "current_role": "Backend Engineer",
        "target_role": "Lead System Architect",
        "years_of_experience": 5,
        "current_skills": ["Java", "Spring Boot", "SQL"]
    }
    response = client.post("/api/v1/career/career-roadmap", json=payload)
    assert response.status_code == 200
    res = response.json()
    assert res["success"] is True
    assert len(res["data"]["milestones"]) > 0

def test_ats_score():
    payload = {
        "user_id": "123e4567-e89b-12d3-a456-426614174000",
        "resume_text": "Java Spring Boot Microservices Redis Kafka PostgreSQL",
        "job_description": "Looking for Senior Engineer with Java 21, Kafka, Redis, and Kubernetes experience."
    }
    response = client.post("/api/v1/resume/ats-score", json=payload)
    assert response.status_code == 200
    res = response.json()
    assert res["data"]["match_percentage"] >= 50
