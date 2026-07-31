package com.careeros.profile.entity;

import com.careeros.profile.entity.enums.EmploymentType;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "experiences",
        indexes = {
                @Index(name = "idx_experiences_profile_id", columnList = "profile_id"),
                @Index(name = "idx_experiences_company_name", columnList = "company_name")
        }
)
@SQLDelete(sql = "UPDATE experiences SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class Experience extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 150)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 30)
    private EmploymentType employmentType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current_role", nullable = false)
    private boolean isCurrentRole;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Experience() {}

    public Experience(UUID id, Profile profile, String companyName, String title, String location, EmploymentType employmentType, LocalDate startDate, LocalDate endDate, boolean isCurrentRole, String description) {
        this.id = id;
        this.profile = profile;
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

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

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

    public static ExperienceBuilder builder() { return new ExperienceBuilder(); }

    public static class ExperienceBuilder {
        private UUID id;
        private Profile profile;
        private String companyName;
        private String title;
        private String location;
        private EmploymentType employmentType;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isCurrentRole;
        private String description;

        public ExperienceBuilder id(UUID id) { this.id = id; return this; }
        public ExperienceBuilder profile(Profile profile) { this.profile = profile; return this; }
        public ExperienceBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public ExperienceBuilder title(String title) { this.title = title; return this; }
        public ExperienceBuilder location(String location) { this.location = location; return this; }
        public ExperienceBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public ExperienceBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ExperienceBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public ExperienceBuilder isCurrentRole(boolean isCurrentRole) { this.isCurrentRole = isCurrentRole; return this; }
        public ExperienceBuilder description(String description) { this.description = description; return this; }

        public Experience build() {
            return new Experience(id, profile, companyName, title, location, employmentType, startDate, endDate, isCurrentRole, description);
        }
    }
}