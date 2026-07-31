package com.careeros.profile.dto.request;

import com.careeros.profile.entity.enums.ProficiencyLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SkillRequest {

    @NotBlank(message = "Skill name is required")
    @Size(max = 100, message = "Skill name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Proficiency level is required")
    private ProficiencyLevel proficiencyLevel;

    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 70, message = "Years of experience cannot exceed 70")
    private Integer yearsOfExperience;

    public SkillRequest() {}

    public SkillRequest(String name, ProficiencyLevel proficiencyLevel, String category, Integer yearsOfExperience) {
        this.name = name;
        this.proficiencyLevel = proficiencyLevel;
        this.category = category;
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProficiencyLevel getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public static SkillRequestBuilder builder() { return new SkillRequestBuilder(); }

    public static class SkillRequestBuilder {
        private String name;
        private ProficiencyLevel proficiencyLevel;
        private String category;
        private Integer yearsOfExperience;

        public SkillRequestBuilder name(String name) { this.name = name; return this; }
        public SkillRequestBuilder proficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }
        public SkillRequestBuilder category(String category) { this.category = category; return this; }
        public SkillRequestBuilder yearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; return this; }

        public SkillRequest build() {
            return new SkillRequest(name, proficiencyLevel, category, yearsOfExperience);
        }
    }
}
