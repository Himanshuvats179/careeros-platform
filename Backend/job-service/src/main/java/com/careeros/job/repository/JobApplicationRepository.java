package com.careeros.job.repository;

import com.careeros.job.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    boolean existsByJobIdAndCandidateId(UUID jobId, UUID candidateId);

    Page<JobApplication> findByCandidateId(UUID candidateId, Pageable pageable);

    Page<JobApplication> findByJobId(UUID jobId, Pageable pageable);

    Optional<JobApplication> findByJobIdAndCandidateId(UUID jobId, UUID candidateId);
}
