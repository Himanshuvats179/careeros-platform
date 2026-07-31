package com.careeros.profile.entity;

import com.careeros.profile.entity.enums.ProficiencyLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(
        name = "languages",
        indexes = {
                @Index(name = "idx_languages_profile_id", columnList = "profile_id"),
                @Index(name = "idx_languages_name", columnList = "language_name")
        }
)
@SQLDelete(sql = "UPDATE languages SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class Language extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "language_name", nullable = false, length = 100)
    private String languageName;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false, length = 30)
    private ProficiencyLevel proficiencyLevel;

    public Language() {}

    public Language(UUID id, Profile profile, String languageName, ProficiencyLevel proficiencyLevel) {
        this.id = id;
        this.profile = profile;
        this.languageName = languageName;
        this.proficiencyLevel = proficiencyLevel;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public String getLanguageName() { return languageName; }
    public void setLanguageName(String languageName) { this.languageName = languageName; }

    public ProficiencyLevel getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public static LanguageBuilder builder() { return new LanguageBuilder(); }

    public static class LanguageBuilder {
        private UUID id;
        private Profile profile;
        private String languageName;
        private ProficiencyLevel proficiencyLevel;

        public LanguageBuilder id(UUID id) { this.id = id; return this; }
        public LanguageBuilder profile(Profile profile) { this.profile = profile; return this; }
        public LanguageBuilder languageName(String languageName) { this.languageName = languageName; return this; }
        public LanguageBuilder proficiencyLevel(ProficiencyLevel proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }

        public Language build() {
            return new Language(id, profile, languageName, proficiencyLevel);
        }
    }
}
