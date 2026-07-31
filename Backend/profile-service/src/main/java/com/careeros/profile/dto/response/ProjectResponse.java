package com.careeros.profile.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class ProjectResponse {
    private UUID id;
    private String title;
    private String description;
    private String projectUrl;
    private String githubUrl;
    private String technologiesUsed;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectResponse() {}

    public ProjectResponse(UUID id, String title, String description, String projectUrl, String githubUrl, String technologiesUsed, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectUrl = projectUrl;
        this.githubUrl = githubUrl;
        this.technologiesUsed = technologiesUsed;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public static ProjectResponseBuilder builder() { return new ProjectResponseBuilder(); }

    public static class ProjectResponseBuilder {
        private UUID id;
        private String title;
        private String description;
        private String projectUrl;
        private String githubUrl;
        private String technologiesUsed;
        private LocalDate startDate;
        private LocalDate endDate;

        public ProjectResponseBuilder id(UUID id) { this.id = id; return this; }
        public ProjectResponseBuilder title(String title) { this.title = title; return this; }
        public ProjectResponseBuilder description(String description) { this.description = description; return this; }
        public ProjectResponseBuilder projectUrl(String projectUrl) { this.projectUrl = projectUrl; return this; }
        public ProjectResponseBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public ProjectResponseBuilder technologiesUsed(String technologiesUsed) { this.technologiesUsed = technologiesUsed; return this; }
        public ProjectResponseBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ProjectResponseBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }

        public ProjectResponse build() {
            return new ProjectResponse(id, title, description, projectUrl, githubUrl, technologiesUsed, startDate, endDate);
        }
    }
}
