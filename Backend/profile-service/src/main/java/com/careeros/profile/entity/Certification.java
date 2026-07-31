package com.careeros.profile.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "certifications",
        indexes = {
                @Index(name = "idx_certifications_profile_id", columnList = "profile_id"),
                @Index(name = "idx_certifications_name", columnList = "name")
        }
)
@SQLDelete(sql = "UPDATE certifications SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class Certification extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "issuing_organization", nullable = false, length = 150)
    private String issuingOrganization;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "credential_id", length = 100)
    private String credentialId;

    @Column(name = "credential_url", length = 255)
    private String credentialUrl;

    public Certification() {}

    public Certification(UUID id, Profile profile, String name, String issuingOrganization, LocalDate issueDate, LocalDate expirationDate, String credentialId, String credentialUrl) {
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.credentialId = credentialId;
        this.credentialUrl = credentialUrl;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIssuingOrganization() { return issuingOrganization; }
    public void setIssuingOrganization(String issuingOrganization) { this.issuingOrganization = issuingOrganization; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public String getCredentialId() { return credentialId; }
    public void setCredentialId(String credentialId) { this.credentialId = credentialId; }

    public String getCredentialUrl() { return credentialUrl; }
    public void setCredentialUrl(String credentialUrl) { this.credentialUrl = credentialUrl; }

    public static CertificationBuilder builder() { return new CertificationBuilder(); }

    public static class CertificationBuilder {
        private UUID id;
        private Profile profile;
        private String name;
        private String issuingOrganization;
        private LocalDate issueDate;
        private LocalDate expirationDate;
        private String credentialId;
        private String credentialUrl;

        public CertificationBuilder id(UUID id) { this.id = id; return this; }
        public CertificationBuilder profile(Profile profile) { this.profile = profile; return this; }
        public CertificationBuilder name(String name) { this.name = name; return this; }
        public CertificationBuilder issuingOrganization(String issuingOrganization) { this.issuingOrganization = issuingOrganization; return this; }
        public CertificationBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public CertificationBuilder expirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; return this; }
        public CertificationBuilder credentialId(String credentialId) { this.credentialId = credentialId; return this; }
        public CertificationBuilder credentialUrl(String credentialUrl) { this.credentialUrl = credentialUrl; return this; }

        public Certification build() {
            return new Certification(id, profile, name, issuingOrganization, issueDate, expirationDate, credentialId, credentialUrl);
        }
    }
}