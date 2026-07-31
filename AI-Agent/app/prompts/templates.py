RESUME_ANALYZE_PROMPT = """
You are an expert Executive Career Advisor and Technical Recruiter with 15+ years of experience at top Fortune 500 tech companies.

Analyze the following resume text for a candidate targeting role: {target_role}.

Resume Text:
{resume_text}

Provide your evaluation strictly in JSON format matching this schema:
{{
  "summary": "2-3 sentence executive summary of the candidate profile",
  "strengths": ["List of 3-5 key candidate strengths"],
  "weaknesses": ["List of 3-5 areas needing improvement"],
  "detected_skills": [
    {{ "category": "Backend/Frontend/Cloud/etc", "skills": ["skill1", "skill2"] }}
  ],
  "formatting_score": 85
}}
"""

RESUME_IMPROVE_PROMPT = """
You are an expert Resume Editor and ATS Optimizer. Rewrite and optimize bullet points from the resume below.
Target Job Description:
{job_description}

Resume Content:
{resume_text}

Return JSON output with improved resume text and specific action items:
{{
  "improved_resume_text": "Enhanced resume markdown text...",
  "action_items": [
    {{
      "section": "Experience / Tech Corp",
      "original": "Built API endpoints",
      "improved": "Architected high-throughput REST APIs handling 10M+ daily requests using Spring Boot 3 & Redis",
      "rationale": "Added quantifiable impact metrics and relevant technologies"
    }}
  ],
  "overall_impact_score": 92
}}
"""

CAREER_ROADMAP_PROMPT = """
Act as a Principal Software Engineering Career Coach.
Create a step-by-step career progression roadmap for a candidate transitioning from {current_role} to {target_role} with {years_of_experience} years of experience.

Current Skills: {current_skills}

Return JSON output:
{{
  "target_role": "{target_role}",
  "estimated_timeline_months": 12,
  "summary": "High-level summary of key transition milestones",
  "milestones": [
    {{
      "phase": "Phase 1: Deepening Core Architecture Skills",
      "timeframe": "Months 1-3",
      "goals": ["Master distributed locking with Redis", "Learn Kubernetes deployment strategies"],
      "recommended_skills": ["Kubernetes", "Kafka", "Redis"]
    }}
  ]
}}
"""

INTERVIEW_QUESTIONS_PROMPT = """
You are a Principal Software Engineer conducting technical interviews for a {target_role} position.
Generate 5 challenging technical and behavioral interview questions focused on: {focus_areas}.

Return JSON output:
{{
  "target_role": "{target_role}",
  "questions": [
    {{
      "id": 1,
      "category": "System Design",
      "question": "How would you design an idempotent audit logging service using Kafka & PostgreSQL?",
      "expected_answer_keypoints": ["Use event ID deduplication", "Dead Letter Queues for unrecoverable errors", "Database indexes"],
      "difficulty": "HARD"
    }}
  ]
}}
"""

COVER_LETTER_PROMPT = """
Generate a persuasive, highly tailored, professional Cover Letter for {user_name} applying for the {target_role} position at {target_company}.

Job Description:
{job_description}

Candidate Profile Summary:
{resume_summary}

Return JSON output:
{{
  "cover_letter_text": "Dear Hiring Team at {target_company}...",
  "key_selling_points": ["Highlight 1", "Highlight 2"]
}}
"""

ATS_SCORE_PROMPT = """
Evaluate how well the resume matches the following job description for ATS (Applicant Tracking System) screeners.

Job Description:
{job_description}

Resume:
{resume_text}

Return JSON output:
{{
  "match_percentage": 82,
  "matched_keywords": ["Java 21", "Spring Boot", "Kafka", "PostgreSQL"],
  "missing_keywords": ["Kubernetes", "AWS Terraform"],
  "formatting_issues": ["Avoid 2-column tabular layouts"],
  "recommendations": ["Incorporate missing keywords naturally into experience bullet points"]
}}
"""
