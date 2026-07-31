package com.careeros.job.controller;

import com.careeros.common.dto.ApiResponse;
import com.careeros.job.dto.request.JobPostingCreateRequest;
import com.careeros.job.dto.request.JobPostingUpdateRequest;
import com.careeros.job.dto.request.JobSearchCriteria;
import com.careeros.job.dto.response.JobPostingResponse;
import com.careeros.job.dto.response.PageResponse;
import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;
import com.careeros.job.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Job Posting Management", description = "APIs for creating, updating, searching, and managing enterprise job postings.")
public class JobPostingController {

    private static final Logger log = LoggerFactory.getLogger(JobPostingController.class);

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @PostMapping
    @Operation(summary = "Create Job Posting", description = "Creates a new job listing and publishes JOB_POSTED event to Kafka.")
    public ResponseEntity<ApiResponse<JobPostingResponse>> createJobPosting(@Valid @RequestBody JobPostingCreateRequest request) {
        log.info("REST request to create job posting title: {}", request.getTitle());
        JobPostingResponse response = jobPostingService.createJobPosting(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job posting created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Job Posting by ID", description = "Retrieves job posting details by UUID primary key (Cached in Redis).")
    public ResponseEntity<ApiResponse<JobPostingResponse>> getJobPostingById(@PathVariable UUID id) {
        log.info("REST request to get job posting ID: {}", id);
        JobPostingResponse response = jobPostingService.getJobPostingById(id);
        return ResponseEntity.ok(ApiResponse.success("Job posting retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Job Posting", description = "Updates existing job listing details and evicts Redis cache.")
    public ResponseEntity<ApiResponse<JobPostingResponse>> updateJobPosting(@PathVariable UUID id, @RequestBody JobPostingUpdateRequest request) {
        log.info("REST request to update job posting ID: {}", id);
        JobPostingResponse response = jobPostingService.updateJobPosting(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job posting updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Job Posting", description = "Soft deletes a job posting from the platform.")
    public ResponseEntity<ApiResponse<Void>> deleteJobPosting(@PathVariable UUID id) {
        log.info("REST request to delete job posting ID: {}", id);
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.ok(ApiResponse.success("Job posting soft deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search Job Postings", description = "Multi-criteria search with pagination, filtering by companyName, location, employmentType, status, and skill keywords.")
    public ResponseEntity<ApiResponse<PageResponse<JobPostingResponse>>> searchJobPostings(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) EmploymentType employmentType,
            @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .companyName(companyName)
                .location(location)
                .employmentType(employmentType)
                .status(status)
                .search(search)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PageResponse<JobPostingResponse> response = jobPostingService.searchJobPostings(criteria);
        return ResponseEntity.ok(ApiResponse.success("Job postings retrieved successfully", response));
    }
}
