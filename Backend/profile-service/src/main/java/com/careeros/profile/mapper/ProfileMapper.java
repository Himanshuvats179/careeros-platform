package com.careeros.profile.mapper;

import com.careeros.profile.dto.request.*;
import com.careeros.profile.dto.response.*;
import com.careeros.profile.entity.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileMapper {

    public Profile toEntity(ProfileCreateRequest request) {
        if (request == null) return null;

        Profile profile = Profile.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .headline(request.getHeadline())
                .bio(request.getBio())
                .phone(request.getPhone())
                .location(request.getLocation())
                .websiteUrl(request.getWebsiteUrl())
                .linkedinUrl(request.getLinkedinUrl())
                .githubUrl(request.getGithubUrl())
                .portfolioUrl(request.getPortfolioUrl())
                .build();

        if (request.getSkills() != null) {
            request.getSkills().forEach(s -> profile.addSkill(toSkillEntity(s)));
        }
        if (request.getExperiences() != null) {
            request.getExperiences().forEach(e -> profile.addExperience(toExperienceEntity(e)));
        }
        if (request.getEducations() != null) {
            request.getEducations().forEach(ed -> profile.addEducation(toEducationEntity(ed)));
        }
        if (request.getProjects() != null) {
            request.getProjects().forEach(p -> profile.addProject(toProjectEntity(p)));
        }
        if (request.getCertifications() != null) {
            request.getCertifications().forEach(c -> profile.addCertification(toCertificationEntity(c)));
        }
        if (request.getLanguages() != null) {
            request.getLanguages().forEach(l -> profile.addLanguage(toLanguageEntity(l)));
        }

        return profile;
    }

    public void updateEntityFromRequest(Profile profile, ProfileUpdateRequest request) {
        if (profile == null || request == null) return;

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setHeadline(request.getHeadline());
        profile.setBio(request.getBio());
        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setWebsiteUrl(request.getWebsiteUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());

        // Clear and rebuild skills
        profile.getSkills().clear();
        if (request.getSkills() != null) {
            request.getSkills().forEach(s -> profile.addSkill(toSkillEntity(s)));
        }

        // Clear and rebuild experiences
        profile.getExperiences().clear();
        if (request.getExperiences() != null) {
            request.getExperiences().forEach(e -> profile.addExperience(toExperienceEntity(e)));
        }

        // Clear and rebuild educations
        profile.getEducations().clear();
        if (request.getEducations() != null) {
            request.getEducations().forEach(ed -> profile.addEducation(toEducationEntity(ed)));
        }

        // Clear and rebuild projects
        profile.getProjects().clear();
        if (request.getProjects() != null) {
            request.getProjects().forEach(p -> profile.addProject(toProjectEntity(p)));
        }

        // Clear and rebuild certifications
        profile.getCertifications().clear();
        if (request.getCertifications() != null) {
            request.getCertifications().forEach(c -> profile.addCertification(toCertificationEntity(c)));
        }

        // Clear and rebuild languages
        profile.getLanguages().clear();
        if (request.getLanguages() != null) {
            request.getLanguages().forEach(l -> profile.addLanguage(toLanguageEntity(l)));
        }
    }

    public ProfileResponse toResponse(Profile profile) {
        if (profile == null) return null;

        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .headline(profile.getHeadline())
                .bio(profile.getBio())
                .phone(profile.getPhone())
                .location(profile.getLocation())
                .websiteUrl(profile.getWebsiteUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .githubUrl(profile.getGithubUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .resumeUrl(profile.getResumeUrl())
                .completionPercentage(calculateCompletionPercentage(profile))
                .version(profile.getVersion())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .skills(mapList(profile.getSkills(), this::toSkillResponse))
                .experiences(mapList(profile.getExperiences(), this::toExperienceResponse))
                .educations(mapList(profile.getEducations(), this::toEducationResponse))
                .projects(mapList(profile.getProjects(), this::toProjectResponse))
                .certifications(mapList(profile.getCertifications(), this::toCertificationResponse))
                .languages(mapList(profile.getLanguages(), this::toLanguageResponse))
                .build();
    }

    public Skill toSkillEntity(SkillRequest req) {
        return Skill.builder()
                .name(req.getName())
                .proficiencyLevel(req.getProficiencyLevel())
                .category(req.getCategory())
                .yearsOfExperience(req.getYearsOfExperience())
                .build();
    }

    public SkillResponse toSkillResponse(Skill entity) {
        return SkillResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .proficiencyLevel(entity.getProficiencyLevel())
                .category(entity.getCategory())
                .yearsOfExperience(entity.getYearsOfExperience())
                .build();
    }

    public Experience toExperienceEntity(ExperienceRequest req) {
        return Experience.builder()
                .companyName(req.getCompanyName())
                .title(req.getTitle())
                .location(req.getLocation())
                .employmentType(req.getEmploymentType())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .isCurrentRole(req.isCurrentRole())
                .description(req.getDescription())
                .build();
    }

    public ExperienceResponse toExperienceResponse(Experience entity) {
        return ExperienceResponse.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .title(entity.getTitle())
                .location(entity.getLocation())
                .employmentType(entity.getEmploymentType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isCurrentRole(entity.isCurrentRole())
                .description(entity.getDescription())
                .build();
    }

    public Education toEducationEntity(EducationRequest req) {
        return Education.builder()
                .institution(req.getInstitution())
                .degreeType(req.getDegreeType())
                .fieldOfStudy(req.getFieldOfStudy())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .grade(req.getGrade())
                .description(req.getDescription())
                .build();
    }

    public EducationResponse toEducationResponse(Education entity) {
        return EducationResponse.builder()
                .id(entity.getId())
                .institution(entity.getInstitution())
                .degreeType(entity.getDegreeType())
                .fieldOfStudy(entity.getFieldOfStudy())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .grade(entity.getGrade())
                .description(entity.getDescription())
                .build();
    }

    public Project toProjectEntity(ProjectRequest req) {
        return Project.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .projectUrl(req.getProjectUrl())
                .githubUrl(req.getGithubUrl())
                .technologiesUsed(req.getTechnologiesUsed())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .build();
    }

    public ProjectResponse toProjectResponse(Project entity) {
        return ProjectResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .projectUrl(entity.getProjectUrl())
                .githubUrl(entity.getGithubUrl())
                .technologiesUsed(entity.getTechnologiesUsed())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    public Certification toCertificationEntity(CertificationRequest req) {
        return Certification.builder()
                .name(req.getName())
                .issuingOrganization(req.getIssuingOrganization())
                .issueDate(req.getIssueDate())
                .expirationDate(req.getExpirationDate())
                .credentialId(req.getCredentialId())
                .credentialUrl(req.getCredentialUrl())
                .build();
    }

    public CertificationResponse toCertificationResponse(Certification entity) {
        return CertificationResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .issuingOrganization(entity.getIssuingOrganization())
                .issueDate(entity.getIssueDate())
                .expirationDate(entity.getExpirationDate())
                .credentialId(entity.getCredentialId())
                .credentialUrl(entity.getCredentialUrl())
                .build();
    }

    public Language toLanguageEntity(LanguageRequest req) {
        return Language.builder()
                .languageName(req.getLanguageName())
                .proficiencyLevel(req.getProficiencyLevel())
                .build();
    }

    public LanguageResponse toLanguageResponse(Language entity) {
        return LanguageResponse.builder()
                .id(entity.getId())
                .languageName(entity.getLanguageName())
                .proficiencyLevel(entity.getProficiencyLevel())
                .build();
    }

    private <T, R> List<R> mapList(List<T> source, java.util.function.Function<T, R> mapperFunction) {
        if (source == null) return Collections.emptyList();
        return source.stream().map(mapperFunction).collect(Collectors.toList());
    }

    public int calculateCompletionPercentage(Profile p) {
        int score = 0;
        if (p.getFirstName() != null && !p.getFirstName().isBlank()) score += 10;
        if (p.getLastName() != null && !p.getLastName().isBlank()) score += 10;
        if (p.getHeadline() != null && !p.getHeadline().isBlank()) score += 15;
        if (p.getBio() != null && !p.getBio().isBlank()) score += 15;
        if (p.getProfilePictureUrl() != null && !p.getProfilePictureUrl().isBlank()) score += 10;
        if (p.getResumeUrl() != null && !p.getResumeUrl().isBlank()) score += 10;
        if (p.getSkills() != null && !p.getSkills().isEmpty()) score += 10;
        if (p.getExperiences() != null && !p.getExperiences().isEmpty()) score += 10;
        if (p.getEducations() != null && !p.getEducations().isEmpty()) score += 10;
        return Math.min(score, 100);
    }
}