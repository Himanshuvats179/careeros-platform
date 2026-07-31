package com.careeros.profile.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "projects",
        indexes = {
                @Index(name = "idx_projects_profile_id", columnList = "profile_id"),
                @Index(name = "idx_projects_title", columnList = "title")
        }
)
@SQLDelete(sql = "UPDATE projects SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class Project extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "project_url", length = 255)
    private String projectUrl;

    @Column(name = "github_url", length = 255)
    private String githubUrl;

    @Column(name = "technologies_used", length = 300)
    private String technologiesUsed;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Project() {}

    public Project(UUID id, Profile profile, String title, String description, String projectUrl, String githubUrl, String technologiesUsed, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.profile = profile;
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

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

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

    public static ProjectBuilder builder() { return new ProjectBuilder(); }

    public static class ProjectBuilder {
        private UUID id;
        private Profile profile;
        private String title;
        private String description;
        private String projectUrl;
        private String githubUrl;
        private String technologiesUsed;
        private LocalDate startDate;
        private LocalDate endDate;

        public ProjectBuilder id(UUID id) { this.id = id; return this; }
        public ProjectBuilder profile(Profile profile) { this.profile = profile; return this; }
        public ProjectBuilder title(String title) { this.title = title; return this; }
        public ProjectBuilder description(String description) { this.description = description; return this; }
        public ProjectBuilder projectUrl(String projectUrl) { this.projectUrl = projectUrl; return this; }
        public ProjectBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public ProjectBuilder technologiesUsed(String technologiesUsed) { this.technologiesUsed = technologiesUsed; return this; }
        public ProjectBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ProjectBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }

        public Project build() {
            return new Project(id, profile, title, description, projectUrl, githubUrl, technologiesUsed, startDate, endDate);
        }
    }
}