package com.careeros.job.dto.request;

import com.careeros.job.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class JobPostingCreateRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Job description is required")
    private String description;

    private String location;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @PositiveOrZero(message = "Minimum salary must be positive or zero")
    private BigDecimal minSalary;

    @PositiveOrZero(message = "Maximum salary must be positive or zero")
    private BigDecimal maxSalary;

    @NotNull(message = "Posted by user ID is required")
    private UUID postedBy;

    private List<String> requiredSkills;

    public JobPostingCreateRequest() {}

    public JobPostingCreateRequest(String title, String companyName, String description, String location, EmploymentType employmentType, BigDecimal minSalary, BigDecimal maxSalary, UUID postedBy, List<String> requiredSkills) {
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.postedBy = postedBy;
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

    public UUID getPostedBy() { return postedBy; }
    public void setPostedBy(UUID postedBy) { this.postedBy = postedBy; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public static JobPostingCreateRequestBuilder builder() { return new JobPostingCreateRequestBuilder(); }

    public static class JobPostingCreateRequestBuilder {
        private String title;
        private String companyName;
        private String description;
        private String location;
        private EmploymentType employmentType = EmploymentType.FULL_TIME;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private UUID postedBy;
        private List<String> requiredSkills;

        public JobPostingCreateRequestBuilder title(String title) { this.title = title; return this; }
        public JobPostingCreateRequestBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobPostingCreateRequestBuilder description(String description) { this.description = description; return this; }
        public JobPostingCreateRequestBuilder location(String location) { this.location = location; return this; }
        public JobPostingCreateRequestBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public JobPostingCreateRequestBuilder minSalary(BigDecimal minSalary) { this.minSalary = minSalary; return this; }
        public JobPostingCreateRequestBuilder maxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; return this; }
        public JobPostingCreateRequestBuilder postedBy(UUID postedBy) { this.postedBy = postedBy; return this; }
        public JobPostingCreateRequestBuilder requiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; return this; }

        public JobPostingCreateRequest build() {
            return new JobPostingCreateRequest(title, companyName, description, location, employmentType, minSalary, maxSalary, postedBy, requiredSkills);
        }
    }
}
