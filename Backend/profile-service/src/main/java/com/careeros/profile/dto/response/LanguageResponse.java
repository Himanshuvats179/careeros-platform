package com.careeros.profile.dto.response;

import com.careeros.profile.entity.enums.ProficiencyLevel;

import java.util.UUID;

public class LanguageResponse {
    private UUID id;
    private String languageName;
    private ProficiencyLevel proficiencyLevel;

    public LanguageResponse() {}

    public LanguageResponse(UUID id, String languageName, ProficiencyLevel proficiencyLevel) {
        this.id = id;
        this.languageName = languageName;
        this.proficiencyLevel = proficiencyLevel;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getLanguageName() { return languageName; }
    public void setLanguageName(String languageName) { this.languageName = languageName; }

    public ProficiencyLevel getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public static LanguageResponseBuilder builder() { return new LanguageResponseBuilder(); }

    public static class LanguageResponseBuilder {
        private UUID id;
        private String languageName;
        private ProficiencyLevel proficiencyLevel;

        public LanguageResponseBuilder id(UUID id) { this.id = id; return this; }
        public LanguageResponseBuilder languageName(String languageName) { this.languageName = languageName; return this; }
        public LanguageResponseBuilder proficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }

        public LanguageResponse build() {
            return new LanguageResponse(id, languageName, proficiencyLevel);
        }
    }
}
