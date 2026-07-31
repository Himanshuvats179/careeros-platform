package com.careeros.job.dto.response;

import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class JobPostingResponse {

    private UUID id;
    private String title;
    private String companyName;
    private String description;
    private String location;
    private EmploymentType employmentType;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private JobStatus status;
    private UUID postedBy;
    private List<String> requiredSkills;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobPostingResponse() {}

    public JobPostingResponse(UUID id, String title, String companyName, String description, String location, EmploymentType employmentType, BigDecimal minSalary, BigDecimal maxSalary, JobStatus status, UUID postedBy, List<String> requiredSkills, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.status = status;
        this.postedBy = postedBy;
        this.requiredSkills = requiredSkills;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public EmploymentType getEmploymentType() { return employmentType; }
    public void setEmploymentType(EmploymentType employmentType) { this.employmentType = employmentType; }

    public BigDecimal getMinSalary() { return minSalary; }
    public void setMinSalary(BigDecimal minSalary) { this.minSalary = minSalary; }

    public BigDecimal getMaxSalary() { return maxSalary; }
    public void setMaxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public UUID getPostedBy() { return postedBy; }
    public void setPostedBy(UUID postedBy) { this.postedBy = postedBy; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static JobPostingResponseBuilder builder() { return new JobPostingResponseBuilder(); }

    public static class JobPostingResponseBuilder {
        private UUID id;
        private String title;
        private String companyName;
        private String description;
        private String location;
        private EmploymentType employmentType;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private JobStatus status;
        private UUID postedBy;
        private List<String> requiredSkills;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public JobPostingResponseBuilder id(UUID id) { this.id = id; return this; }
        public JobPostingResponseBuilder title(String title) { this.title = title; return this; }
        public JobPostingResponseBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobPostingResponseBuilder description(String description) { this.description = description; return this; }
        public JobPostingResponseBuilder location(String location) { this.location = location; return this; }
        public JobPostingResponseBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public JobPostingResponseBuilder minSalary(BigDecimal minSalary) { this.minSalary = minSalary; return this; }
        public JobPostingResponseBuilder maxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; return this; }
        public JobPostingResponseBuilder status(JobStatus status) { this.status = status; return this; }
        public JobPostingResponseBuilder postedBy(UUID postedBy) { this.postedBy = postedBy; return this; }
        public JobPostingResponseBuilder requiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; return this; }
        public JobPostingResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public JobPostingResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public JobPostingResponse build() {
            return new JobPostingResponse(id, title, companyName, description, location, employmentType, minSalary, maxSalary, status, postedBy, requiredSkills, createdAt, updatedAt);
        }
    }
}
