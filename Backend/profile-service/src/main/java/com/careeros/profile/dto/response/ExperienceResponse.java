package com.careeros.profile.dto.response;

import com.careeros.profile.entity.enums.EmploymentType;

import java.time.LocalDate;
import java.util.UUID;

public class ExperienceResponse {
    private UUID id;
    private String companyName;
    private String title;
    private String location;
    private EmploymentType employmentType;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrentRole;
    private String description;

    public ExperienceResponse() {}

    public ExperienceResponse(UUID id, String companyName, String title, String location, EmploymentType employmentType, LocalDate startDate, LocalDate endDate, boolean isCurrentRole, String description) {
        this.id = id;
        this.companyName = companyName;
        this.title = title;
        this.location = location;
        this.employmentType = employmentType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrentRole = isCurrentRole;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public static ExperienceResponseBuilder builder() { return new ExperienceResponseBuilder(); }

    public static class ExperienceResponseBuilder {
        private UUID id;
        private String companyName;
        private String title;
        private String location;
        private EmploymentType employmentType;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isCurrentRole;
        private String description;

        public ExperienceResponseBuilder id(UUID id) { this.id = id; return this; }
        public ExperienceResponseBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public ExperienceResponseBuilder title(String title) { this.title = title; return this; }
        public ExperienceResponseBuilder location(String location) { this.location = location; return this; }
        public ExperienceResponseBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public ExperienceResponseBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ExperienceResponseBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public ExperienceResponseBuilder isCurrentRole(boolean isCurrentRole) { this.isCurrentRole = isCurrentRole; return this; }
        public ExperienceResponseBuilder description(String description) { this.description = description; return this; }

        public ExperienceResponse build() {
            return new ExperienceResponse(id, companyName, title, location, employmentType, startDate, endDate, isCurrentRole, description);
        }
    }
}
