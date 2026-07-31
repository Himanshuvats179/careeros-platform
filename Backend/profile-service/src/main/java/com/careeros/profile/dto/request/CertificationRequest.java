package com.careeros.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public class CertificationRequest {

    @NotBlank(message = "Certification name is required")
    @Size(max = 150, message = "Certification name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Issuing organization is required")
    @Size(max = 150, message = "Issuing organization must not exceed 150 characters")
    private String issuingOrganization;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    private LocalDate expirationDate;

    @Size(max = 100, message = "Credential ID must not exceed 100 characters")
    private String credentialId;

    @URL(message = "Invalid credential URL format")
    private String credentialUrl;

    public CertificationRequest() {}

    public CertificationRequest(String name, String issuingOrganization, LocalDate issueDate, LocalDate expirationDate, String credentialId, String credentialUrl) {
        this.name = name;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.credentialId = credentialId;
        this.credentialUrl = credentialUrl;
    }

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

    public static CertificationRequestBuilder builder() { return new CertificationRequestBuilder(); }

    public static class CertificationRequestBuilder {
        private String name;
        private String issuingOrganization;
        private LocalDate issueDate;
        private LocalDate expirationDate;
        private String credentialId;
        private String credentialUrl;

        public CertificationRequestBuilder name(String name) { this.name = name; return this; }
        public CertificationRequestBuilder issuingOrganization(String issuingOrganization) { this.issuingOrganization = issuingOrganization; return this; }
        public CertificationRequestBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public CertificationRequestBuilder expirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; return this; }
        public CertificationRequestBuilder credentialId(String credentialId) { this.credentialId = credentialId; return this; }
        public CertificationRequestBuilder credentialUrl(String credentialUrl) { this.credentialUrl = credentialUrl; return this; }

        public CertificationRequest build() {
            return new CertificationRequest(name, issuingOrganization, issueDate, expirationDate, credentialId, credentialUrl);
        }
    }
}
