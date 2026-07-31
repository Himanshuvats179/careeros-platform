package com.careeros.job.entity;

import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_postings", indexes = {
        @Index(name = "idx_job_company", columnList = "company_name"),
        @Index(name = "idx_job_status", columnList = "status"),
        @Index(name = "idx_job_title", columnList = "title")
})
@SQLDelete(sql = "UPDATE job_postings SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class JobPosting extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 30)
    private EmploymentType employmentType;

    @Column(name = "min_salary")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    private BigDecimal maxSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JobStatus status = JobStatus.OPEN;

    @Column(name = "posted_by", nullable = false)
    private UUID postedBy;

    @ElementCollection
    @CollectionTable(name = "job_required_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill_name")
    private List<String> requiredSkills = new ArrayList<>();

    public JobPosting() {}

    public JobPosting(UUID id, String title, String companyName, String description, String location, EmploymentType employmentType, BigDecimal minSalary, BigDecimal maxSalary, JobStatus status, UUID postedBy, List<String> requiredSkills) {
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.status = status;
        this.postedBy = postedBy;
        if (requiredSkills != null) this.requiredSkills = requiredSkills;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public EmploymentType getEmploymentType() { return employmentType; }
    public void setEmploymentType(EmploymentType employmentType) { this.employmentType = employmentType; }

    public BigDecimal getMinSalary() { return minSalary; }
    public void setMinSalary(BigDecimal minSalary) { this.minSalary = minSalary; }

    public BigDecimal getMaxSalary() { return maxSalary; }
    public void setMaxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public UUID getPostedBy() { return postedBy; }
    public void setPostedBy(UUID postedBy) { this.postedBy = postedBy; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public static JobPostingBuilder builder() { return new JobPostingBuilder(); }

    public static class JobPostingBuilder {
        private UUID id;
        private String title;
        private String companyName;
        private String description;
        private String location;
        private EmploymentType employmentType = EmploymentType.FULL_TIME;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private JobStatus status = JobStatus.OPEN;
        private UUID postedBy;
        private List<String> requiredSkills = new ArrayList<>();

        public JobPostingBuilder id(UUID id) { this.id = id; return this; }
        public JobPostingBuilder title(String title) { this.title = title; return this; }
        public JobPostingBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobPostingBuilder description(String description) { this.description = description; return this; }
        public JobPostingBuilder location(String location) { this.location = location; return this; }
        public JobPostingBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public JobPostingBuilder minSalary(BigDecimal minSalary) { this.minSalary = minSalary; return this; }
        public JobPostingBuilder maxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; return this; }
        public JobPostingBuilder status(JobStatus status) { this.status = status; return this; }
        public JobPostingBuilder postedBy(UUID postedBy) { this.postedBy = postedBy; return this; }
        public JobPostingBuilder requiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; return this; }

        public JobPosting build() {
            return new JobPosting(id, title, companyName, description, location, employmentType, minSalary, maxSalary, status, postedBy, requiredSkills);
        }
    }
}
