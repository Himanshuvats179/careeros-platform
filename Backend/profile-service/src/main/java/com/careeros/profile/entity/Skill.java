package com.careeros.profile.entity;

import com.careeros.profile.entity.enums.ProficiencyLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(
        name = "skills",
        indexes = {
                @Index(name = "idx_skills_profile_id", columnList = "profile_id"),
                @Index(name = "idx_skills_name", columnList = "name")
        }
)
@SQLDelete(sql = "UPDATE skills SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class Skill extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false, length = 30)
    private ProficiencyLevel proficiencyLevel;

    @Column(length = 50)
    private String category;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    public Skill() {}

    public Skill(UUID id, Profile profile, String name, ProficiencyLevel proficiencyLevel, String category, Integer yearsOfExperience) {
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.proficiencyLevel = proficiencyLevel;
        this.category = category;
        this.yearsOfExperience = yearsOfExperience;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProficiencyLevel getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public static SkillBuilder builder() { return new SkillBuilder(); }

    public static class SkillBuilder {
        private UUID id;
        private Profile profile;
        private String name;
        private ProficiencyLevel proficiencyLevel;
        private String category;
        private Integer yearsOfExperience;

        public SkillBuilder id(UUID id) { this.id = id; return this; }
        public SkillBuilder profile(Profile profile) { this.profile = profile; return this; }
        public SkillBuilder name(String name) { this.name = name; return this; }
        public SkillBuilder proficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }
        public SkillBuilder category(String category) { this.category = category; return this; }
        public SkillBuilder yearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; return this; }

        public Skill build() {
            return new Skill(id, profile, name, proficiencyLevel, category, yearsOfExperience);
        }
    }
}