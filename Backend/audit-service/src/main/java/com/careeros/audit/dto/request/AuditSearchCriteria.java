package com.careeros.audit.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditSearchCriteria {

    private UUID userId;
    private String serviceName;
    private String eventType;
    private String status;
    private String search;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int page = 0;
    private int size = 20;
    private String sortBy = "timestamp";
    private String sortDirection = "DESC";

    public AuditSearchCriteria() {}

    public AuditSearchCriteria(UUID userId, String serviceName, String eventType, String status, String search, LocalDateTime startDate, LocalDateTime endDate, int page, int size, String sortBy, String sortDirection) {
        this.userId = userId;
        this.serviceName = serviceName;
        this.eventType = eventType;
        this.status = status;
        this.search = search;
        this.startDate = startDate;
        this.endDate = endDate;
        this.page = page;
        this.size = size;
        if (sortBy != null) this.sortBy = sortBy;
        if (sortDirection != null) this.sortDirection = sortDirection;
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

    public static AuditSearchCriteriaBuilder builder() { return new AuditSearchCriteriaBuilder(); }

    public static class AuditSearchCriteriaBuilder {
        private UUID userId;
        private String serviceName;
        private String eventType;
        private String status;
        private String search;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private int page = 0;
        private int size = 20;
        private String sortBy = "timestamp";
        private String sortDirection = "DESC";

        public AuditSearchCriteriaBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AuditSearchCriteriaBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public AuditSearchCriteriaBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public AuditSearchCriteriaBuilder status(String status) { this.status = status; return this; }
        public AuditSearchCriteriaBuilder search(String search) { this.search = search; return this; }
        public AuditSearchCriteriaBuilder startDate(LocalDateTime startDate) { this.startDate = startDate; return this; }
        public AuditSearchCriteriaBuilder endDate(LocalDateTime endDate) { this.endDate = endDate; return this; }
        public AuditSearchCriteriaBuilder page(int page) { this.page = page; return this; }
        public AuditSearchCriteriaBuilder size(int size) { this.size = size; return this; }
        public AuditSearchCriteriaBuilder sortBy(String sortBy) { this.sortBy = sortBy; return this; }
        public AuditSearchCriteriaBuilder sortDirection(String sortDirection) { this.sortDirection = sortDirection; return this; }

        public AuditSearchCriteria build() {
            return new AuditSearchCriteria(userId, serviceName, eventType, status, search, startDate, endDate, page, size, sortBy, sortDirection);
        }
    }
}
