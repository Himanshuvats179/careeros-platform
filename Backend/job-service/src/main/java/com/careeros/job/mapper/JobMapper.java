package com.careeros.job.mapper;

import com.careeros.job.dto.request.JobPostingCreateRequest;
import com.careeros.job.dto.response.JobApplicationResponse;
import com.careeros.job.dto.response.JobPostingResponse;
import com.careeros.job.entity.JobApplication;
import com.careeros.job.entity.JobPosting;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JobMapper {

    public JobPosting toEntity(JobPostingCreateRequest req) {
        if (req == null) return null;

        return JobPosting.builder()
                .title(req.getTitle())
                .companyName(req.getCompanyName())
                .description(req.getDescription())
                .location(req.getLocation())
                .employmentType(req.getEmploymentType())
                .minSalary(req.getMinSalary())
                .maxSalary(req.getMaxSalary())
                .postedBy(req.getPostedBy())
                .requiredSkills(req.getRequiredSkills() != null ? req.getRequiredSkills() : new ArrayList<>())
                .build();
    }

    public JobPostingResponse toResponse(JobPosting entity) {
        if (entity == null) return null;

        return JobPostingResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .companyName(entity.getCompanyName())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .employmentType(entity.getEmploymentType())
                .minSalary(entity.getMinSalary())
                .maxSalary(entity.getMaxSalary())
                .status(entity.getStatus())
                .postedBy(entity.getPostedBy())
                .requiredSkills(entity.getRequiredSkills())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public JobApplicationResponse toResponse(JobApplication entity) {
        if (entity == null) return null;

        return JobApplicationResponse.builder()
                .id(entity.getId())
                .jobId(entity.getJob() != null ? entity.getJob().getId() : null)
                .jobTitle(entity.getJob() != null ? entity.getJob().getTitle() : null)
                .companyName(entity.getJob() != null ? entity.getJob().getCompanyName() : null)
                .candidateId(entity.getCandidateId())
                .coverLetterText(entity.getCoverLetterText())
                .resumeUrl(entity.getResumeUrl())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
