package com.careeros.job.controller;

import com.careeros.job.dto.request.JobPostingCreateRequest;
import com.careeros.job.dto.response.JobPostingResponse;
import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;
import com.careeros.job.service.JobPostingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobPostingController.class)
class JobPostingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobPostingService jobPostingService;

    private UUID jobId;
    private UUID postedBy;
    private JobPostingResponse response;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
        postedBy = UUID.randomUUID();

        response = JobPostingResponse.builder()
                .id(jobId)
                .title("Lead Software Architect")
                .companyName("CareerOS")
                .employmentType(EmploymentType.FULL_TIME)
                .status(JobStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/jobs - Should create job posting and return 201 Created")
    void createJobPosting_ShouldReturn201() throws Exception {
        JobPostingCreateRequest request = JobPostingCreateRequest.builder()
                .title("Lead Software Architect")
                .companyName("CareerOS")
                .description("Build microservices platform")
                .employmentType(EmploymentType.FULL_TIME)
                .postedBy(postedBy)
                .build();

        when(jobPostingService.createJobPosting(any(JobPostingCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Lead Software Architect"));
    }

    @Test
    @DisplayName("GET /api/v1/jobs/{id} - Should return job posting and 200 OK")
    void getJobPostingById_ShouldReturn200() throws Exception {
        when(jobPostingService.getJobPostingById(jobId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/jobs/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(jobId.toString()));
    }
}
