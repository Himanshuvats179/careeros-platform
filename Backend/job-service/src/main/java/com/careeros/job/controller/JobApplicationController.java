package com.careeros.job.controller;

import com.careeros.common.dto.ApiResponse;
import com.careeros.job.dto.request.JobApplicationCreateRequest;
import com.careeros.job.dto.response.JobApplicationResponse;
import com.careeros.job.dto.response.PageResponse;
import com.careeros.job.enums.ApplicationStatus;
import com.careeros.job.service.JobApplicationService;
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
@RequestMapping("/api/v1/applications")
@Tag(name = "Job Application Management", description = "APIs for candidate job submissions and application pipeline status transitions.")
public class JobApplicationController {

    private static final Logger log = LoggerFactory.getLogger(JobApplicationController.class);

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    @Operation(summary = "Apply for Job", description = "Submits a new job application and publishes JOB_APPLIED event to Kafka.")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> applyForJob(@Valid @RequestBody JobApplicationCreateRequest request) {
        log.info("REST request for candidate {} to apply for job {}", request.getCandidateId(), request.getJobId());
        JobApplicationResponse response = jobApplicationService.applyForJob(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job application submitted successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Application by ID", description = "Retrieves job application record by UUID primary key.")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> getApplicationById(@PathVariable UUID id) {
        log.info("REST request to get job application ID: {}", id);
        JobApplicationResponse response = jobApplicationService.getApplicationById(id);
        return ResponseEntity.ok(ApiResponse.success("Job application retrieved successfully", response));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update Application Status", description = "Transitions application pipeline status (APPLIED -> SCREENED -> INTERVIEW_SCHEDULED -> OFFER_EXTENDED).")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateApplicationStatus(@PathVariable UUID id, @RequestParam ApplicationStatus status) {
        log.info("REST request to update application ID {} to status {}", id, status);
        JobApplicationResponse response = jobApplicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", response));
    }

    @GetMapping("/candidate/{candidateId}")
    @Operation(summary = "Get Applications by Candidate", description = "Retrieves paginated applications submitted by a specific candidate.")
    public ResponseEntity<ApiResponse<PageResponse<JobApplicationResponse>>> getApplicationsByCandidateId(
            @PathVariable UUID candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST request to get applications for candidate ID: {}", candidateId);
        PageResponse<JobApplicationResponse> response = jobApplicationService.getApplicationsByCandidateId(candidateId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Candidate applications retrieved successfully", response));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "Get Applications for Job", description = "Retrieves paginated applications received for a specific job posting.")
    public ResponseEntity<ApiResponse<PageResponse<JobApplicationResponse>>> getApplicationsByJobId(
            @PathVariable UUID jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST request to get applications for job ID: {}", jobId);
        PageResponse<JobApplicationResponse> response = jobApplicationService.getApplicationsByJobId(jobId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Job applications retrieved successfully", response));
    }
}
