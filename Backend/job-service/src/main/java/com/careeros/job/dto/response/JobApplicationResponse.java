package com.careeros.job.dto.response;

import com.careeros.job.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class JobApplicationResponse {

    private UUID id;
    private UUID jobId;
    private String jobTitle;
    private String companyName;
    private UUID candidateId;
    private String coverLetterText;
    private String resumeUrl;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobApplicationResponse() {}

    public JobApplicationResponse(UUID id, UUID jobId, String jobTitle, String companyName, UUID candidateId, String coverLetterText, String resumeUrl, ApplicationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.candidateId = candidateId;
        this.coverLetterText = coverLetterText;
        this.resumeUrl = resumeUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getJobId() { return jobId; }
    public void setJobId(UUID jobId) { this.jobId = jobId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public String getCoverLetterText() { return coverLetterText; }
    public void setCoverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static JobApplicationResponseBuilder builder() { return new JobApplicationResponseBuilder(); }

    public static class JobApplicationResponseBuilder {
        private UUID id;
        private UUID jobId;
        private String jobTitle;
        private String companyName;
        private UUID candidateId;
        private String coverLetterText;
        private String resumeUrl;
        private ApplicationStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public JobApplicationResponseBuilder id(UUID id) { this.id = id; return this; }
        public JobApplicationResponseBuilder jobId(UUID jobId) { this.jobId = jobId; return this; }
        public JobApplicationResponseBuilder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public JobApplicationResponseBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobApplicationResponseBuilder candidateId(UUID candidateId) { this.candidateId = candidateId; return this; }
        public JobApplicationResponseBuilder coverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; return this; }
        public JobApplicationResponseBuilder resumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; return this; }
        public JobApplicationResponseBuilder status(ApplicationStatus status) { this.status = status; return this; }
        public JobApplicationResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public JobApplicationResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public JobApplicationResponse build() {
            return new JobApplicationResponse(id, jobId, jobTitle, companyName, candidateId, coverLetterText, resumeUrl, status, createdAt, updatedAt);
        }
    }
}
