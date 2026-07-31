package com.careeros.profile.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "profiles",
        indexes = {
                @Index(name = "idx_profiles_user_id", columnList = "user_id", unique = true),
                @Index(name = "idx_profiles_location", columnList = "location"),
                @Index(name = "idx_profiles_headline", columnList = "headline"),
                @Index(name = "idx_profiles_is_deleted", columnList = "is_deleted")
        }
)
@SQLDelete(sql = "UPDATE profiles SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class Profile extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 150)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 30)
    private String phone;

    @Column(length = 150)
    private String location;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "github_url", length = 255)
    private String githubUrl;

    @Column(name = "portfolio_url", length = 255)
    private String portfolioUrl;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Certification> certifications = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Language> languages = new ArrayList<>();

    public Profile() {}

    public Profile(UUID id, UUID userId, String firstName, String lastName, String headline, String bio, String phone, String location, String websiteUrl, String linkedinUrl, String githubUrl, String portfolioUrl, String profilePictureUrl, String resumeUrl, List<Skill> skills, List<Experience> experiences, List<Education> educations, List<Project> projects, List<Certification> certifications, List<Language> languages) {
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
        if (skills != null) this.skills = skills;
        if (experiences != null) this.experiences = experiences;
        if (educations != null) this.educations = educations;
        if (projects != null) this.projects = projects;
        if (certifications != null) this.certifications = certifications;
        if (languages != null) this.languages = languages;
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

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public List<Experience> getExperiences() { return experiences; }
    public void setExperiences(List<Experience> experiences) { this.experiences = experiences; }

    public List<Education> getEducations() { return educations; }
    public void setEducations(List<Education> educations) { this.educations = educations; }

    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }

    public List<Certification> getCertifications() { return certifications; }
    public void setCertifications(List<Certification> certifications) { this.certifications = certifications; }

    public List<Language> getLanguages() { return languages; }
    public void setLanguages(List<Language> languages) { this.languages = languages; }

    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.setProfile(this);
    }
    public void removeSkill(Skill skill) {
        skills.remove(skill);
        skill.setProfile(null);
    }

    public void addExperience(Experience experience) {
        experiences.add(experience);
        experience.setProfile(this);
    }
    public void removeExperience(Experience experience) {
        experiences.remove(experience);
        experience.setProfile(null);
    }

    public void addEducation(Education education) {
        educations.add(education);
        education.setProfile(this);
    }
    public void removeEducation(Education education) {
        educations.remove(education);
        education.setProfile(null);
    }

    public void addProject(Project project) {
        projects.add(project);
        project.setProfile(this);
    }
    public void removeProject(Project project) {
        projects.remove(project);
        project.setProfile(null);
    }

    public void addCertification(Certification certification) {
        certifications.add(certification);
        certification.setProfile(this);
    }
    public void removeCertification(Certification certification) {
        certifications.remove(certification);
        certification.setProfile(null);
    }

    public void addLanguage(Language language) {
        languages.add(language);
        language.setProfile(this);
    }
    public void removeLanguage(Language language) {
        languages.remove(language);
        language.setProfile(null);
    }

    public static ProfileBuilder builder() { return new ProfileBuilder(); }

    public static class ProfileBuilder {
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
        private List<Skill> skills = new ArrayList<>();
        private List<Experience> experiences = new ArrayList<>();
        private List<Education> educations = new ArrayList<>();
        private List<Project> projects = new ArrayList<>();
        private List<Certification> certifications = new ArrayList<>();
        private List<Language> languages = new ArrayList<>();

        public ProfileBuilder id(UUID id) { this.id = id; return this; }
        public ProfileBuilder userId(UUID userId) { this.userId = userId; return this; }
        public ProfileBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public ProfileBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public ProfileBuilder headline(String headline) { this.headline = headline; return this; }
        public ProfileBuilder bio(String bio) { this.bio = bio; return this; }
        public ProfileBuilder phone(String phone) { this.phone = phone; return this; }
        public ProfileBuilder location(String location) { this.location = location; return this; }
        public ProfileBuilder websiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; return this; }
        public ProfileBuilder linkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; return this; }
        public ProfileBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public ProfileBuilder portfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; return this; }
        public ProfileBuilder profilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; return this; }
        public ProfileBuilder resumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; return this; }
        public ProfileBuilder skills(List<Skill> skills) { this.skills = skills; return this; }
        public ProfileBuilder experiences(List<Experience> experiences) { this.experiences = experiences; return this; }
        public ProfileBuilder educations(List<Education> educations) { this.educations = educations; return this; }
        public ProfileBuilder projects(List<Project> projects) { this.projects = projects; return this; }
        public ProfileBuilder certifications(List<Certification> certifications) { this.certifications = certifications; return this; }
        public ProfileBuilder languages(List<Language> languages) { this.languages = languages; return this; }

        public Profile build() {
            return new Profile(id, userId, firstName, lastName, headline, bio, phone, location, websiteUrl, linkedinUrl, githubUrl, portfolioUrl, profilePictureUrl, resumeUrl, skills, experiences, educations, projects, certifications, languages);
        }
    }
}
