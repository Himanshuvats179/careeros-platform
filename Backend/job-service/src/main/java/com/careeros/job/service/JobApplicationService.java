package com.careeros.job.service;

import com.careeros.job.dto.request.JobApplicationCreateRequest;
import com.careeros.job.dto.response.JobApplicationResponse;
import com.careeros.job.dto.response.PageResponse;
import com.careeros.job.enums.ApplicationStatus;

import java.util.UUID;

public interface JobApplicationService {
    JobApplicationResponse applyForJob(JobApplicationCreateRequest request);
    JobApplicationResponse getApplicationById(UUID id);
    JobApplicationResponse updateApplicationStatus(UUID id, ApplicationStatus status);
    PageResponse<JobApplicationResponse> getApplicationsByCandidateId(UUID candidateId, int page, int size);
    PageResponse<JobApplicationResponse> getApplicationsByJobId(UUID jobId, int page, int size);
}
