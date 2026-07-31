package com.careeros.job.service.impl;

import com.careeros.job.dto.event.JobEvent;
import com.careeros.job.dto.request.JobPostingCreateRequest;
import com.careeros.job.dto.request.JobPostingUpdateRequest;
import com.careeros.job.dto.request.JobSearchCriteria;
import com.careeros.job.dto.response.JobPostingResponse;
import com.careeros.job.dto.response.PageResponse;
import com.careeros.job.entity.JobPosting;
import com.careeros.job.exception.ResourceNotFoundException;
import com.careeros.job.mapper.JobMapper;
import com.careeros.job.repository.JobPostingRepository;
import com.careeros.job.service.JobPostingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    private static final Logger log = LoggerFactory.getLogger(JobPostingServiceImpl.class);
    private static final String KAFKA_TOPIC = "careeros.job.events";

    private final JobPostingRepository jobPostingRepository;
    private final JobMapper jobMapper;
    private final KafkaTemplate<String, JobEvent> kafkaTemplate;

    public JobPostingServiceImpl(JobPostingRepository jobPostingRepository, JobMapper jobMapper, KafkaTemplate<String, JobEvent> kafkaTemplate) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobMapper = jobMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public JobPostingResponse createJobPosting(JobPostingCreateRequest request) {
        log.info("Creating new job posting title: '{}' for company: '{}'", request.getTitle(), request.getCompanyName());

        JobPosting jobPosting = jobMapper.toEntity(request);
        JobPosting savedJob = jobPostingRepository.save(jobPosting);

        // Publish JOB_POSTED event to Kafka
        publishKafkaEvent("JOB_POSTED", savedJob.getPostedBy(), "Posted new job: " + savedJob.getTitle(), savedJob.getId().toString());

        return jobMapper.toResponse(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "jobs", key = "#id")
    public JobPostingResponse getJobPostingById(UUID id) {
        log.info("Fetching job posting ID: {}", id);
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with ID: " + id));
        return jobMapper.toResponse(jobPosting);
    }

    @Override
    @Transactional
    @CacheEvict(value = "jobs", key = "#id")
    public JobPostingResponse updateJobPosting(UUID id, JobPostingUpdateRequest request) {
        log.info("Updating job posting ID: {}", id);
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with ID: " + id));

        if (request.getTitle() != null) jobPosting.setTitle(request.getTitle());
        if (request.getCompanyName() != null) jobPosting.setCompanyName(request.getCompanyName());
        if (request.getDescription() != null) jobPosting.setDescription(request.getDescription());
        if (request.getLocation() != null) jobPosting.setLocation(request.getLocation());
        if (request.getEmploymentType() != null) jobPosting.setEmploymentType(request.getEmploymentType());
        if (request.getMinSalary() != null) jobPosting.setMinSalary(request.getMinSalary());
        if (request.getMaxSalary() != null) jobPosting.setMaxSalary(request.getMaxSalary());
        if (request.getStatus() != null) jobPosting.setStatus(request.getStatus());
        if (request.getRequiredSkills() != null) jobPosting.setRequiredSkills(request.getRequiredSkills());

        JobPosting updatedJob = jobPostingRepository.save(jobPosting);
        return jobMapper.toResponse(updatedJob);
    }

    @Override
    @Transactional
    @CacheEvict(value = "jobs", key = "#id")
    public void deleteJobPosting(UUID id) {
        log.info("Soft deleting job posting ID: {}", id);
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with ID: " + id));

        jobPosting.setDeleted(true);
        jobPostingRepository.save(jobPosting);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobPostingResponse> searchJobPostings(JobSearchCriteria criteria) {
        Sort sort = Sort.by(
                Sort.Direction.fromString(criteria.getSortDirection()),
                criteria.getSortBy()
        );
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Page<JobPosting> pageResult = jobPostingRepository.searchJobPostings(
                criteria.getCompanyName(),
                criteria.getLocation(),
                criteria.getEmploymentType(),
                criteria.getStatus(),
                criteria.getSearch(),
                pageable
        );

        Page<JobPostingResponse> responsePage = pageResult.map(jobMapper::toResponse);
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
