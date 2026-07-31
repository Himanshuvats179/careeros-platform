package com.careeros.job.entity;

import com.careeros.job.enums.ApplicationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "job_applications", indexes = {
        @Index(name = "idx_app_candidate", columnList = "candidate_id"),
        @Index(name = "idx_app_job", columnList = "job_id"),
        @Index(name = "idx_app_status", columnList = "status")
})
@SQLDelete(sql = "UPDATE job_applications SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class JobApplication extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting job;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "cover_letter_text", columnDefinition = "TEXT")
    private String coverLetterText;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    public JobApplication() {}

    public JobApplication(UUID id, JobPosting job, UUID candidateId, String coverLetterText, String resumeUrl, ApplicationStatus status) {
        this.id = id;
        this.job = job;
        this.candidateId = candidateId;
        this.coverLetterText = coverLetterText;
        this.resumeUrl = resumeUrl;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public JobPosting getJob() { return job; }
    public void setJob(JobPosting job) { this.job = job; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public String getCoverLetterText() { return coverLetterText; }
    public void setCoverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public static JobApplicationBuilder builder() { return new JobApplicationBuilder(); }

    public static class JobApplicationBuilder {
        private UUID id;
        private JobPosting job;
        private UUID candidateId;
        private String coverLetterText;
        private String resumeUrl;
        private ApplicationStatus status = ApplicationStatus.APPLIED;

        public JobApplicationBuilder id(UUID id) { this.id = id; return this; }
        public JobApplicationBuilder job(JobPosting job) { this.job = job; return this; }
        public JobApplicationBuilder candidateId(UUID candidateId) { this.candidateId = candidateId; return this; }
        public JobApplicationBuilder coverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; return this; }
        public JobApplicationBuilder resumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; return this; }
        public JobApplicationBuilder status(ApplicationStatus status) { this.status = status; return this; }

        public JobApplication build() {
            return new JobApplication(id, job, candidateId, coverLetterText, resumeUrl, status);
        }
    }
}
