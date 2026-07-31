package com.careeros.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public class ProjectRequest {

    @NotBlank(message = "Project title is required")
    @Size(max = 150, message = "Project title must not exceed 150 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @URL(message = "Invalid project URL format")
    private String projectUrl;

    @URL(message = "Invalid GitHub URL format")
    private String githubUrl;

    @Size(max = 300, message = "Technologies used must not exceed 300 characters")
    private String technologiesUsed;

    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectRequest() {}

    public ProjectRequest(String title, String description, String projectUrl, String githubUrl, String technologiesUsed, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.projectUrl = projectUrl;
        this.githubUrl = githubUrl;
        this.technologiesUsed = technologiesUsed;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProjectUrl() { return projectUrl; }
    public void setProjectUrl(String projectUrl) { this.projectUrl = projectUrl; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public String getTechnologiesUsed() { return technologiesUsed; }
    public void setTechnologiesUsed(String technologiesUsed) { this.technologiesUsed = technologiesUsed; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public static ProjectRequestBuilder builder() { return new ProjectRequestBuilder(); }

    public static class ProjectRequestBuilder {
        private String title;
        private String description;
        private String projectUrl;
        private String githubUrl;
        private String technologiesUsed;
        private LocalDate startDate;
        private LocalDate endDate;

        public ProjectRequestBuilder title(String title) { this.title = title; return this; }
        public ProjectRequestBuilder description(String description) { this.description = description; return this; }
        public ProjectRequestBuilder projectUrl(String projectUrl) { this.projectUrl = projectUrl; return this; }
        public ProjectRequestBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public ProjectRequestBuilder technologiesUsed(String technologiesUsed) { this.technologiesUsed = technologiesUsed; return this; }
        public ProjectRequestBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ProjectRequestBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }

        public ProjectRequest build() {
            return new ProjectRequest(title, description, projectUrl, githubUrl, technologiesUsed, startDate, endDate);
        }
    }
}
