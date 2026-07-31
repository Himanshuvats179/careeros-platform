package com.careeros.job.dto.request;

import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;

public class JobSearchCriteria {

    private String search;
    private String companyName;
    private String location;
    private EmploymentType employmentType;
    private JobStatus status;

    private int page = 0;
    private int size = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";

    public JobSearchCriteria() {}

    public JobSearchCriteria(String search, String companyName, String location, EmploymentType employmentType, JobStatus status, int page, int size, String sortBy, String sortDirection) {
        this.search = search;
        this.companyName = companyName;
        this.location = location;
        this.employmentType = employmentType;
        this.status = status;
        this.page = page;
        this.size = size;
        if (sortBy != null) this.sortBy = sortBy;
        if (sortDirection != null) this.sortDirection = sortDirection;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public EmploymentType getEmploymentType() { return employmentType; }
    public void setEmploymentType(EmploymentType employmentType) { this.employmentType = employmentType; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

    public static JobSearchCriteriaBuilder builder() { return new JobSearchCriteriaBuilder(); }

    public static class JobSearchCriteriaBuilder {
        private String search;
        private String companyName;
        private String location;
        private EmploymentType employmentType;
        private JobStatus status;
        private int page = 0;
        private int size = 20;
        private String sortBy = "createdAt";
        private String sortDirection = "DESC";

        public JobSearchCriteriaBuilder search(String search) { this.search = search; return this; }
        public JobSearchCriteriaBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobSearchCriteriaBuilder location(String location) { this.location = location; return this; }
        public JobSearchCriteriaBuilder employmentType(EmploymentType employmentType) { this.employmentType = employmentType; return this; }
        public JobSearchCriteriaBuilder status(JobStatus status) { this.status = status; return this; }
        public JobSearchCriteriaBuilder page(int page) { this.page = page; return this; }
        public JobSearchCriteriaBuilder size(int size) { this.size = size; return this; }
        public JobSearchCriteriaBuilder sortBy(String sortBy) { this.sortBy = sortBy; return this; }
        public JobSearchCriteriaBuilder sortDirection(String sortDirection) { this.sortDirection = sortDirection; return this; }

        public JobSearchCriteria build() {
            return new JobSearchCriteria(search, companyName, location, employmentType, status, page, size, sortBy, sortDirection);
        }
    }
}
