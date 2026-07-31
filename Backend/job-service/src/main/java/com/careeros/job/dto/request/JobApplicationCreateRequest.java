package com.careeros.job.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class JobApplicationCreateRequest {

    @NotNull(message = "Job ID is required")
    private UUID jobId;

    @NotNull(message = "Candidate ID is required")
    private UUID candidateId;

    private String coverLetterText;
    private String resumeUrl;

    public JobApplicationCreateRequest() {}

    public JobApplicationCreateRequest(UUID jobId, UUID candidateId, String coverLetterText, String resumeUrl) {
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.coverLetterText = coverLetterText;
        this.resumeUrl = resumeUrl;
    }

    public UUID getJobId() { return jobId; }
    public void setJobId(UUID jobId) { this.jobId = jobId; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public String getCoverLetterText() { return coverLetterText; }
    public void setCoverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public static JobApplicationCreateRequestBuilder builder() { return new JobApplicationCreateRequestBuilder(); }

    public static class JobApplicationCreateRequestBuilder {
        private UUID jobId;
        private UUID candidateId;
        private String coverLetterText;
        private String resumeUrl;

        public JobApplicationCreateRequestBuilder jobId(UUID jobId) { this.jobId = jobId; return this; }
        public JobApplicationCreateRequestBuilder candidateId(UUID candidateId) { this.candidateId = candidateId; return this; }
        public JobApplicationCreateRequestBuilder coverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; return this; }
        public JobApplicationCreateRequestBuilder resumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; return this; }

        public JobApplicationCreateRequest build() {
            return new JobApplicationCreateRequest(jobId, candidateId, coverLetterText, resumeUrl);
        }
    }
}
