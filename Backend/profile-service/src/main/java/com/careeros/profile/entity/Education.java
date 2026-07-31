package com.careeros.profile.entity;

import com.careeros.profile.entity.enums.DegreeType;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "educations",
        indexes = {
                @Index(name = "idx_educations_profile_id", columnList = "profile_id"),
                @Index(name = "idx_educations_institution", columnList = "institution")
        }
)
@SQLDelete(sql = "UPDATE educations SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class Education extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 150)
    private String institution;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree_type", nullable = false, length = 30)
    private DegreeType degreeType;

    @Column(name = "field_of_study", nullable = false, length = 150)
    private String fieldOfStudy;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 20)
    private String grade;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Education() {}

    public Education(UUID id, Profile profile, String institution, DegreeType degreeType, String fieldOfStudy, LocalDate startDate, LocalDate endDate, String grade, String description) {
        this.id = id;
        this.profile = profile;
        this.institution = institution;
        this.degreeType = degreeType;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public DegreeType getDegreeType() { return degreeType; }
    public void setDegreeType(DegreeType degreeType) { this.degreeType = degreeType; }

    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static EducationBuilder builder() { return new EducationBuilder(); }

    public static class EducationBuilder {
        private UUID id;
        private Profile profile;
        private String institution;
        private DegreeType degreeType;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
        private String grade;
        private String description;

        public EducationBuilder id(UUID id) { this.id = id; return this; }
        public EducationBuilder profile(Profile profile) { this.profile = profile; return this; }
        public EducationBuilder institution(String institution) { this.institution = institution; return this; }
        public EducationBuilder degreeType(DegreeType degreeType) { this.degreeType = degreeType; return this; }
        public EducationBuilder fieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; return this; }
        public EducationBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public EducationBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public EducationBuilder grade(String grade) { this.grade = grade; return this; }
        public EducationBuilder description(String description) { this.description = description; return this; }

        public Education build() {
            return new Education(id, profile, institution, degreeType, fieldOfStudy, startDate, endDate, grade, description);
        }
    }
}