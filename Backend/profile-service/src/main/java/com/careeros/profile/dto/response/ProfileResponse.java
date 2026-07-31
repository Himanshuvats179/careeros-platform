package com.careeros.profile.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProfileResponse {

    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String bio;
    private String phone;
    private String location;
    private String websiteUrl;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String profilePictureUrl;
    private String resumeUrl;
    private int completionPercentage;

    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<SkillResponse> skills;
    private List<ExperienceResponse> experiences;
    private List<EducationResponse> educations;
    private List<ProjectResponse> projects;
    private List<CertificationResponse> certifications;
    private List<LanguageResponse> languages;

    public ProfileResponse() {}

    public ProfileResponse(UUID id, UUID userId, String firstName, String lastName, String headline, String bio, String phone, String location, String websiteUrl, String linkedinUrl, String githubUrl, String portfolioUrl, String profilePictureUrl, String resumeUrl, int completionPercentage, Long version, LocalDateTime createdAt, LocalDateTime updatedAt, List<SkillResponse> skills, List<ExperienceResponse> experiences, List<EducationResponse> educations, List<ProjectResponse> projects, List<CertificationResponse> certifications, List<LanguageResponse> languages) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headline = headline;
        this.bio = bio;
        this.phone = phone;
        this.location = location;
        this.websiteUrl = websiteUrl;
        this.linkedinUrl = linkedinUrl;
        this.githubUrl = githubUrl;
        this.portfolioUrl = portfolioUrl;
        this.profilePictureUrl = profilePictureUrl;
        this.resumeUrl = resumeUrl;
        this.completionPercentage = completionPercentage;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.skills = skills;
        this.experiences = experiences;
        this.educations = educations;
        this.projects = projects;
        this.certifications = certifications;
        this.languages = languages;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public int getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<SkillResponse> getSkills() { return skills; }
    public void setSkills(List<SkillResponse> skills) { this.skills = skills; }

    public List<ExperienceResponse> getExperiences() { return experiences; }
    public void setExperiences(List<ExperienceResponse> experiences) { this.experiences = experiences; }

    public List<EducationResponse> getEducations() { return educations; }
    public void setEducations(List<EducationResponse> educations) { this.educations = educations; }

    public List<ProjectResponse> getProjects() { return projects; }
    public void setProjects(List<ProjectResponse> projects) { this.projects = projects; }

    public List<CertificationResponse> getCertifications() { return certifications; }
    public void setCertifications(List<CertificationResponse> certifications) { this.certifications = certifications; }

    public List<LanguageResponse> getLanguages() { return languages; }
    public void setLanguages(List<LanguageResponse> languages) { this.languages = languages; }

    public static ProfileResponseBuilder builder() { return new ProfileResponseBuilder(); }

    public static class ProfileResponseBuilder {
        private UUID id;
        private UUID userId;
        private String firstName;
        private String lastName;
        private String headline;
        private String bio;
        private String phone;
        private String location;
        private String websiteUrl;
        private String linkedinUrl;
        private String githubUrl;
        private String portfolioUrl;
        private String profilePictureUrl;
        private String resumeUrl;
        private int completionPercentage;
        private Long version;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<SkillResponse> skills;
        private List<ExperienceResponse> experiences;
        private List<EducationResponse> educations;
        private List<ProjectResponse> projects;
        private List<CertificationResponse> certifications;
        private List<LanguageResponse> languages;

        public ProfileResponseBuilder id(UUID id) { this.id = id; return this; }
        public ProfileResponseBuilder userId(UUID userId) { this.userId = userId; return this; }
        public ProfileResponseBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public ProfileResponseBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public ProfileResponseBuilder headline(String headline) { this.headline = headline; return this; }
        public ProfileResponseBuilder bio(String bio) { this.bio = bio; return this; }
        public ProfileResponseBuilder phone(String phone) { this.phone = phone; return this; }
        public ProfileResponseBuilder location(String location) { this.location = location; return this; }
        public ProfileResponseBuilder websiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; return this; }
        public ProfileResponseBuilder linkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; return this; }
        public ProfileResponseBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public ProfileResponseBuilder portfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; return this; }
        public ProfileResponseBuilder profilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; return this; }
        public ProfileResponseBuilder resumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; return this; }
        public ProfileResponseBuilder completionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; return this; }
        public ProfileResponseBuilder version(Long version) { this.version = version; return this; }
        public ProfileResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ProfileResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public ProfileResponseBuilder skills(List<SkillResponse> skills) { this.skills = skills; return this; }
        public ProfileResponseBuilder experiences(List<ExperienceResponse> experiences) { this.experiences = experiences; return this; }
        public ProfileResponseBuilder educations(List<EducationResponse> educations) { this.educations = educations; return this; }
        public ProfileResponseBuilder projects(List<ProjectResponse> projects) { this.projects = projects; return this; }
        public ProfileResponseBuilder certifications(List<CertificationResponse> certifications) { this.certifications = certifications; return this; }
        public ProfileResponseBuilder languages(List<LanguageResponse> languages) { this.languages = languages; return this; }

        public ProfileResponse build() {
            return new ProfileResponse(id, userId, firstName, lastName, headline, bio, phone, location, websiteUrl, linkedinUrl, githubUrl, portfolioUrl, profilePictureUrl, resumeUrl, completionPercentage, version, createdAt, updatedAt, skills, experiences, educations, projects, certifications, languages);
        }
    }
}