package com.careeros.profile.dto.response;

import com.careeros.profile.entity.enums.ProficiencyLevel;

import java.util.UUID;

public class SkillResponse {
    private UUID id;
    private String name;
    private ProficiencyLevel proficiencyLevel;
    private String category;
    private Integer yearsOfExperience;

    public SkillResponse() {}

    public SkillResponse(UUID id, String name, ProficiencyLevel proficiencyLevel, String category, Integer yearsOfExperience) {
        this.id = id;
        this.name = name;
        this.proficiencyLevel = proficiencyLevel;
        this.category = category;
        this.yearsOfExperience = yearsOfExperience;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProficiencyLevel getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public static SkillResponseBuilder builder() { return new SkillResponseBuilder(); }

    public static class SkillResponseBuilder {
        private UUID id;
        private String name;
        private ProficiencyLevel proficiencyLevel;
        private String category;
        private Integer yearsOfExperience;

        public SkillResponseBuilder id(UUID id) { this.id = id; return this; }
        public SkillResponseBuilder name(String name) { this.name = name; return this; }
        public SkillResponseBuilder proficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }
        public SkillResponseBuilder category(String category) { this.category = category; return this; }
        public SkillResponseBuilder yearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; return this; }

        public SkillResponse build() {
            return new SkillResponse(id, name, proficiencyLevel, category, yearsOfExperience);
        }
    }
}
