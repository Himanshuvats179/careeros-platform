package com.careeros.profile.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class CertificationResponse {
    private UUID id;
    private String name;
    private String issuingOrganization;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private String credentialId;
    private String credentialUrl;

    public CertificationResponse() {}

    public CertificationResponse(UUID id, String name, String issuingOrganization, LocalDate issueDate, LocalDate expirationDate, String credentialId, String credentialUrl) {
        this.id = id;
        this.name = name;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.credentialId = credentialId;
        this.credentialUrl = credentialUrl;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public static CertificationResponseBuilder builder() { return new CertificationResponseBuilder(); }

    public static class CertificationResponseBuilder {
        private UUID id;
        private String name;
        private String issuingOrganization;
        private LocalDate issueDate;
        private LocalDate expirationDate;
        private String credentialId;
        private String credentialUrl;

        public CertificationResponseBuilder id(UUID id) { this.id = id; return this; }
        public CertificationResponseBuilder name(String name) { this.name = name; return this; }
        public CertificationResponseBuilder issuingOrganization(String issuingOrganization) { this.issuingOrganization = issuingOrganization; return this; }
        public CertificationResponseBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public CertificationResponseBuilder expirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; return this; }
        public CertificationResponseBuilder credentialId(String credentialId) { this.credentialId = credentialId; return this; }
        public CertificationResponseBuilder credentialUrl(String credentialUrl) { this.credentialUrl = credentialUrl; return this; }

        public CertificationResponse build() {
            return new CertificationResponse(id, name, issuingOrganization, issueDate, expirationDate, credentialId, credentialUrl);
        }
    }
}
