package com.careeros.profile.dto.request;

import com.careeros.profile.entity.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ExperienceRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String companyName;

    @NotBlank(message = "Job title is required")
    @Size(max = 150, message = "Job title must not exceed 150 characters")
    private String title;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;
    private boolean isCurrentRole;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    public ExperienceRequest() {}

    public ExperienceRequest(String companyName, String title, String location, EmploymentType employmentType, LocalDate startDate, LocalDate endDate, boolean isCurrentRole, String description) {
        this.companyName = companyName;
        this.title = title;
        this.location = location;
        this.employmentType = employmentType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrentRole = isCurrentRole;
        this.description = description;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public EmploymentType getEmploymentType() { return employmentType; }
    public void setEmploymentType(EmploymentType employmentType) { this.employmentType = employmentType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isCurrentRole() { return isCurrentRole; }
    public void setCurrentRole(boolean currentRole) { isCurrentRole = currentRole; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static ExperienceRequestBuilder builder() { return new ExperienceRequestBuilder(); }

    public static class ExperienceRequestBuilder {
        private String companyName;
        private String title;
        private String location;
        private EmploymentType employmentType;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isCurrentRole;
        private String description;

        public ExperienceRequestBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public ExperienceRequestBuilder title(String title) { this.title = title; return this; }
        public ExperienceRequestBuilder location(String location) { this.location = location; return this; }
        public ExperienceRequestBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public ExperienceRequestBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ExperienceRequestBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public ExperienceRequestBuilder isCurrentRole(boolean isCurrentRole) { this.isCurrentRole = isCurrentRole; return this; }
        public ExperienceRequestBuilder description(String description) { this.description = description; return this; }

        public ExperienceRequest build() {
            return new ExperienceRequest(companyName, title, location, employmentType, startDate, endDate, isCurrentRole, description);
        }
    }
}
