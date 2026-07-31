package com.careeros.job.repository;

import com.careeros.job.entity.JobPosting;
import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, UUID>, JpaSpecificationExecutor<JobPosting> {

    @Query("""
        SELECT j FROM JobPosting j
        WHERE (:companyName IS NULL OR LOWER(j.companyName) = LOWER(:companyName))
          AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
          AND (:employmentType IS NULL OR j.employmentType = :employmentType)
          AND (:status IS NULL OR j.status = :status)
          AND (:search IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(j.description) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<JobPosting> searchJobPostings(
            @Param("companyName") String companyName,
            @Param("location") String location,
            @Param("employmentType") EmploymentType employmentType,
            @Param("status") JobStatus status,
            @Param("search") String search,
            Pageable pageable
    );
}
