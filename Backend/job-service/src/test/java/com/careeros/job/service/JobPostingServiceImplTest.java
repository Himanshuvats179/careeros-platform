package com.careeros.job.service;

import com.careeros.job.dto.event.JobEvent;
import com.careeros.job.dto.request.JobPostingCreateRequest;
import com.careeros.job.dto.response.JobPostingResponse;
import com.careeros.job.entity.JobPosting;
import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;
import com.careeros.job.mapper.JobMapper;
import com.careeros.job.repository.JobPostingRepository;
import com.careeros.job.service.impl.JobPostingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobPostingServiceImplTest {

    @Mock
    private JobPostingRepository jobPostingRepository;

    @Mock
    private JobMapper jobMapper;

    @Mock
    private KafkaTemplate<String, JobEvent> kafkaTemplate;

    @InjectMocks
    private JobPostingServiceImpl jobPostingService;

    private UUID jobId;
    private UUID postedBy;
    private JobPosting jobPosting;
    private JobPostingResponse jobPostingResponse;
    private JobPostingCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
        postedBy = UUID.randomUUID();

        createRequest = JobPostingCreateRequest.builder()
                .title("Senior Software Architect")
                .companyName("CareerOS")
                .description("Build event-driven microservices with Spring Boot and Kafka")
                .location("San Francisco, CA")
                .employmentType(EmploymentType.FULL_TIME)
                .minSalary(BigDecimal.valueOf(180000))
                .maxSalary(BigDecimal.valueOf(250000))
                .postedBy(postedBy)
                .requiredSkills(List.of("Java 21", "Spring Boot", "Kafka"))
                .build();

        jobPosting = JobPosting.builder()
                .id(jobId)
                .title("Senior Software Architect")
                .companyName("CareerOS")
                .description("Build event-driven microservices with Spring Boot and Kafka")
                .location("San Francisco, CA")
                .employmentType(EmploymentType.FULL_TIME)
                .minSalary(BigDecimal.valueOf(180000))
                .maxSalary(BigDecimal.valueOf(250000))
                .status(JobStatus.OPEN)
                .postedBy(postedBy)
                .requiredSkills(List.of("Java 21", "Spring Boot", "Kafka"))
                .build();

        jobPostingResponse = JobPostingResponse.builder()
                .id(jobId)
                .title("Senior Software Architect")
                .companyName("CareerOS")
                .status(JobStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create job posting and publish Kafka event")
    void createJobPosting_Success() {
        when(jobMapper.toEntity(createRequest)).thenReturn(jobPosting);
        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(jobPosting);
        when(jobMapper.toResponse(jobPosting)).thenReturn(jobPostingResponse);

        JobPostingResponse response = jobPostingService.createJobPosting(createRequest);

        assertNotNull(response);
        assertEquals(jobId, response.getId());
        verify(jobPostingRepository).save(jobPosting);
        verify(kafkaTemplate).send(eq("careeros.job.events"), anyString(), any(JobEvent.class));
    }

    @Test
    @DisplayName("Should retrieve job posting by ID")
    void getJobPostingById_Success() {
        when(jobPostingRepository.findById(jobId)).thenReturn(Optional.of(jobPosting));
        when(jobMapper.toResponse(jobPosting)).thenReturn(jobPostingResponse);

        JobPostingResponse response = jobPostingService.getJobPostingById(jobId);

        assertNotNull(response);
        assertEquals(jobId, response.getId());
    }
}
