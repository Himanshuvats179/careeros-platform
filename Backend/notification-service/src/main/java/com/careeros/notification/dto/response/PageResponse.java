package com.careeros.notification.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private boolean isFirst;

    public PageResponse() {}

    public PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean isLast, boolean isFirst) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
        this.isFirst = isFirst;
    }

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isLast() { return isLast; }
    public void setLast(boolean last) { isLast = last; }

    public boolean isFirst() { return isFirst; }
    public void setFirst(boolean first) { isFirst = first; }

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .isFirst(page.isFirst())
                .build();
    }

    public static <T> PageResponseBuilder<T> builder() { return new PageResponseBuilder<T>(); }

    public static class PageResponseBuilder<T> {
        private List<T> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean isLast;
        private boolean isFirst;

        public PageResponseBuilder<T> content(List<T> content) { this.content = content; return this; }
        public PageResponseBuilder<T> pageNumber(int pageNumber) { this.pageNumber = pageNumber; return this; }
        public PageResponseBuilder<T> pageSize(int pageSize) { this.pageSize = pageSize; return this; }
        public PageResponseBuilder<T> totalElements(long totalElements) { this.totalElements = totalElements; return this; }
        public PageResponseBuilder<T> totalPages(int totalPages) { this.totalPages = totalPages; return this; }
        public PageResponseBuilder<T> isLast(boolean isLast) { this.isLast = isLast; return this; }
        public PageResponseBuilder<T> isFirst(boolean isFirst) { this.isFirst = isFirst; return this; }

        public PageResponse<T> build() {
            return new PageResponse<T>(content, pageNumber, pageSize, totalElements, totalPages, isLast, isFirst);
        }
    }
}
