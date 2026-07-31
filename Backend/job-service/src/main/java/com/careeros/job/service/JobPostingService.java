package com.careeros.job.service;

import com.careeros.job.dto.request.JobPostingCreateRequest;
import com.careeros.job.dto.request.JobPostingUpdateRequest;
import com.careeros.job.dto.request.JobSearchCriteria;
import com.careeros.job.dto.response.JobPostingResponse;
import com.careeros.job.dto.response.PageResponse;

import java.util.UUID;

public interface JobPostingService {
    JobPostingResponse createJobPosting(JobPostingCreateRequest request);
    JobPostingResponse getJobPostingById(UUID id);
    JobPostingResponse updateJobPosting(UUID id, JobPostingUpdateRequest request);
    void deleteJobPosting(UUID id);
    PageResponse<JobPostingResponse> searchJobPostings(JobSearchCriteria criteria);
}
