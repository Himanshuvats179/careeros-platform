package com.careeros.profile.dto.request;

import com.careeros.profile.entity.enums.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LanguageRequest {

    @NotBlank(message = "Language name is required")
    @Size(max = 100, message = "Language name must not exceed 100 characters")
    private String languageName;

    @NotNull(message = "Proficiency level is required")
    private ProficiencyLevel proficiencyLevel;

    public LanguageRequest() {}

    public LanguageRequest(String languageName, ProficiencyLevel proficiencyLevel) {
        this.languageName = languageName;
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getLanguageName() { return languageName; }
    public void setLanguageName(String languageName) { this.languageName = languageName; }

    public ProficiencyLevel getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public static LanguageRequestBuilder builder() { return new LanguageRequestBuilder(); }

    public static class LanguageRequestBuilder {
        private String languageName;
        private ProficiencyLevel proficiencyLevel;

        public LanguageRequestBuilder languageName(String languageName) { this.languageName = languageName; return this; }
        public LanguageRequestBuilder proficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }

        public LanguageRequest build() {
            return new LanguageRequest(languageName, proficiencyLevel);
        }
    }
}
