package com.careeros.profile.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.UUID;

public class ProfileCreateRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Size(max = 150, message = "Headline must not exceed 150 characters")
    private String headline;

    @Size(max = 2000, message = "Bio must not exceed 2000 characters")
    private String bio;

    @Pattern(regexp = "^$|^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
    private String phone;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @URL(message = "Invalid website URL format")
    private String websiteUrl;

    @URL(message = "Invalid LinkedIn URL format")
    private String linkedinUrl;

    @URL(message = "Invalid GitHub URL format")
    private String githubUrl;

    @URL(message = "Invalid Portfolio URL format")
    private String portfolioUrl;

    @Valid
    private List<SkillRequest> skills;

    @Valid
    private List<ExperienceRequest> experiences;

    @Valid
    private List<EducationRequest> educations;

    @Valid
    private List<ProjectRequest> projects;

    @Valid
    private List<CertificationRequest> certifications;

    @Valid
    private List<LanguageRequest> languages;

    public ProfileCreateRequest() {}

    public ProfileCreateRequest(UUID userId, String firstName, String lastName, String headline, String bio, String phone, String location, String websiteUrl, String linkedinUrl, String githubUrl, String portfolioUrl, List<SkillRequest> skills, List<ExperienceRequest> experiences, List<EducationRequest> educations, List<ProjectRequest> projects, List<CertificationRequest> certifications, List<LanguageRequest> languages) {
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
        this.skills = skills;
        this.experiences = experiences;
        this.educations = educations;
        this.projects = projects;
        this.certifications = certifications;
        this.languages = languages;
    }

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

    public List<SkillRequest> getSkills() { return skills; }
    public void setSkills(List<SkillRequest> skills) { this.skills = skills; }

    public List<ExperienceRequest> getExperiences() { return experiences; }
    public void setExperiences(List<ExperienceRequest> experiences) { this.experiences = experiences; }

    public List<EducationRequest> getEducations() { return educations; }
    public void setEducations(List<EducationRequest> educations) { this.educations = educations; }

    public List<ProjectRequest> getProjects() { return projects; }
    public void setProjects(List<ProjectRequest> projects) { this.projects = projects; }

    public List<CertificationRequest> getCertifications() { return certifications; }
    public void setCertifications(List<CertificationRequest> certifications) { this.certifications = certifications; }

    public List<LanguageRequest> getLanguages() { return languages; }
    public void setLanguages(List<LanguageRequest> languages) { this.languages = languages; }

    public static ProfileCreateRequestBuilder builder() { return new ProfileCreateRequestBuilder(); }

    public static class ProfileCreateRequestBuilder {
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
        private List<SkillRequest> skills;
        private List<ExperienceRequest> experiences;
        private List<EducationRequest> educations;
        private List<ProjectRequest> projects;
        private List<CertificationRequest> certifications;
        private List<LanguageRequest> languages;

        public ProfileCreateRequestBuilder userId(UUID userId) { this.userId = userId; return this; }
        public ProfileCreateRequestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public ProfileCreateRequestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public ProfileCreateRequestBuilder headline(String headline) { this.headline = headline; return this; }
        public ProfileCreateRequestBuilder bio(String bio) { this.bio = bio; return this; }
        public ProfileCreateRequestBuilder phone(String phone) { this.phone = phone; return this; }
        public ProfileCreateRequestBuilder location(String location) { this.location = location; return this; }
        public ProfileCreateRequestBuilder websiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; return this; }
        public ProfileCreateRequestBuilder linkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; return this; }
        public ProfileCreateRequestBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public ProfileCreateRequestBuilder portfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; return this; }
        public ProfileCreateRequestBuilder skills(List<SkillRequest> skills) { this.skills = skills; return this; }
        public ProfileCreateRequestBuilder experiences(List<ExperienceRequest> experiences) { this.experiences = experiences; return this; }
        public ProfileCreateRequestBuilder educations(List<EducationRequest> educations) { this.educations = educations; return this; }
        public ProfileCreateRequestBuilder projects(List<ProjectRequest> projects) { this.projects = projects; return this; }
        public ProfileCreateRequestBuilder certifications(List<CertificationRequest> certifications) { this.certifications = certifications; return this; }
        public ProfileCreateRequestBuilder languages(List<LanguageRequest> languages) { this.languages = languages; return this; }

        public ProfileCreateRequest build() {
            return new ProfileCreateRequest(userId, firstName, lastName, headline, bio, phone, location, websiteUrl, linkedinUrl, githubUrl, portfolioUrl, skills, experiences, educations, projects, certifications, languages);
        }
    }
}
