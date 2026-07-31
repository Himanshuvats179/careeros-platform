package com.careeros.job.service.impl;

import com.careeros.job.dto.event.JobEvent;
import com.careeros.job.dto.request.JobApplicationCreateRequest;
import com.careeros.job.dto.response.JobApplicationResponse;
import com.careeros.job.dto.response.PageResponse;
import com.careeros.job.entity.JobApplication;
import com.careeros.job.entity.JobPosting;
import com.careeros.job.enums.ApplicationStatus;
import com.careeros.job.exception.JobServiceException;
import com.careeros.job.exception.ResourceNotFoundException;
import com.careeros.job.mapper.JobMapper;
import com.careeros.job.repository.JobApplicationRepository;
import com.careeros.job.repository.JobPostingRepository;
import com.careeros.job.service.JobApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private static final Logger log = LoggerFactory.getLogger(JobApplicationServiceImpl.class);
    private static final String KAFKA_TOPIC = "careeros.job.events";

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JobMapper jobMapper;
    private final KafkaTemplate<String, JobEvent> kafkaTemplate;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository, JobPostingRepository jobPostingRepository, JobMapper jobMapper, KafkaTemplate<String, JobEvent> kafkaTemplate) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.jobMapper = jobMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public JobApplicationResponse applyForJob(JobApplicationCreateRequest request) {
        log.info("Candidate {} applying for job {}", request.getCandidateId(), request.getJobId());

        JobPosting job = jobPostingRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with ID: " + request.getJobId()));

        if (jobApplicationRepository.existsByJobIdAndCandidateId(request.getJobId(), request.getCandidateId())) {
            throw new JobServiceException("Candidate has already applied for job ID: " + request.getJobId());
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .candidateId(request.getCandidateId())
                .coverLetterText(request.getCoverLetterText())
                .resumeUrl(request.getResumeUrl())
                .status(ApplicationStatus.APPLIED)
                .build();

        JobApplication savedApp = jobApplicationRepository.save(application);

        // Publish JOB_APPLIED event to Kafka
        publishKafkaEvent("JOB_APPLIED", savedApp.getCandidateId(), "Applied for job: " + job.getTitle(), savedApp.getId().toString());

        return jobMapper.toResponse(savedApp);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getApplicationById(UUID id) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + id));
        return jobMapper.toResponse(application);
    }

    @Override
    @Transactional
    public JobApplicationResponse updateApplicationStatus(UUID id, ApplicationStatus status) {
        log.info("Updating application ID {} status to {}", id, status);
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + id));

        application.setStatus(status);
        JobApplication updated = jobApplicationRepository.save(application);

        publishKafkaEvent("APPLICATION_STATUS_UPDATED", updated.getCandidateId(), "Application status updated to: " + status, updated.getId().toString());

        return jobMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobApplicationResponse> getApplicationsByCandidateId(UUID candidateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobApplication> pageResult = jobApplicationRepository.findByCandidateId(candidateId, pageable);
        Page<JobApplicationResponse> responsePage = pageResult.map(jobMapper::toResponse);
        return PageResponse.fromPage(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobApplicationResponse> getApplicationsByJobId(UUID jobId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobApplication> pageResult = jobApplicationRepository.findByJobId(jobId, pageable);
        Page<JobApplicationResponse> responsePage = pageResult.map(jobMapper::toResponse);
        return PageResponse.fromPage(responsePage);
    }

    private void publishKafkaEvent(String eventType, UUID userId, String action, String responseData) {
        try {
            JobEvent event = JobEvent.builder()
                    .userId(userId)
                    .eventType(eventType)
                    .action(action)
                    .responseData(responseData)
                    .build();
            kafkaTemplate.send(KAFKA_TOPIC, event.getEventId().toString(), event);
        } catch (Exception e) {
            log.error("Failed to publish Kafka event for action {}: {}", action, e.getMessage());
        }
    }
}
