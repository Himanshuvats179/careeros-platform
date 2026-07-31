package com.careeros.profile.dto.request;

public class ProfileSearchCriteria {

    private String search;
    private String skill;
    private String location;
    private String company;
    private String degree;
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";

    public ProfileSearchCriteria() {}

    public ProfileSearchCriteria(String search, String skill, String location, String company, String degree, int page, int size, String sortBy, String sortDirection) {
        this.search = search;
        this.skill = skill;
        this.location = location;
        this.company = company;
        this.degree = degree;
        this.page = page;
        this.size = size;
        if (sortBy != null) this.sortBy = sortBy;
        if (sortDirection != null) this.sortDirection = sortDirection;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

    public static ProfileSearchCriteriaBuilder builder() { return new ProfileSearchCriteriaBuilder(); }

    public static class ProfileSearchCriteriaBuilder {
        private String search;
        private String skill;
        private String location;
        private String company;
        private String degree;
        private int page = 0;
        private int size = 10;
        private String sortBy = "createdAt";
        private String sortDirection = "DESC";

        public ProfileSearchCriteriaBuilder search(String search) { this.search = search; return this; }
        public ProfileSearchCriteriaBuilder skill(String skill) { this.skill = skill; return this; }
        public ProfileSearchCriteriaBuilder location(String location) { this.location = location; return this; }
        public ProfileSearchCriteriaBuilder company(String company) { this.company = company; return this; }
        public ProfileSearchCriteriaBuilder degree(String degree) { this.degree = degree; return this; }
        public ProfileSearchCriteriaBuilder page(int page) { this.page = page; return this; }
        public ProfileSearchCriteriaBuilder size(int size) { this.size = size; return this; }
        public ProfileSearchCriteriaBuilder sortBy(String sortBy) { this.sortBy = sortBy; return this; }
        public ProfileSearchCriteriaBuilder sortDirection(String sortDirection) { this.sortDirection = sortDirection; return this; }

        public ProfileSearchCriteria build() {
            return new ProfileSearchCriteria(search, skill, location, company, degree, page, size, sortBy, sortDirection);
        }
    }
}
