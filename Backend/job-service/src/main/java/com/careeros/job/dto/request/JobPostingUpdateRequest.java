package com.careeros.job.dto.request;

import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;

import java.math.BigDecimal;
import java.util.List;

public class JobPostingUpdateRequest {

    private String title;
    private String companyName;
    private String description;
    private String location;
    private EmploymentType employmentType;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private JobStatus status;
    private List<String> requiredSkills;

    public JobPostingUpdateRequest() {}

    public JobPostingUpdateRequest(String title, String companyName, String description, String location, EmploymentType employmentType, BigDecimal minSalary, BigDecimal maxSalary, JobStatus status, List<String> requiredSkills) {
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.status = status;
        this.requiredSkills = requiredSkills;
    }

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

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public static JobPostingUpdateRequestBuilder builder() { return new JobPostingUpdateRequestBuilder(); }

    public static class JobPostingUpdateRequestBuilder {
        private String title;
        private String companyName;
        private String description;
        private String location;
        private EmploymentType employmentType;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private JobStatus status;
        private List<String> requiredSkills;

        public JobPostingUpdateRequestBuilder title(String title) { this.title = title; return this; }
        public JobPostingUpdateRequestBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobPostingUpdateRequestBuilder description(String description) { this.description = description; return this; }
        public JobPostingUpdateRequestBuilder location(String location) { this.location = location; return this; }
        public JobPostingUpdateRequestBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public JobPostingUpdateRequestBuilder minSalary(BigDecimal minSalary) { this.minSalary = minSalary; return this; }
        public JobPostingUpdateRequestBuilder maxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; return this; }
        public JobPostingUpdateRequestBuilder status(JobStatus status) { this.status = status; return this; }
        public JobPostingUpdateRequestBuilder requiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; return this; }

        public JobPostingUpdateRequest build() {
            return new JobPostingUpdateRequest(title, companyName, description, location, employmentType, minSalary, maxSalary, status, requiredSkills);
        }
    }
}
