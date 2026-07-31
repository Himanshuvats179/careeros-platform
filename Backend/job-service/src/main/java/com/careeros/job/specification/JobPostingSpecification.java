package com.careeros.job.specification;

import com.careeros.job.entity.JobPosting;
import com.careeros.job.enums.EmploymentType;
import com.careeros.job.enums.JobStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JobPostingSpecification {

    public static Specification<JobPosting> filterJobs(
            String keyword,
            String companyName,
            String location,
            EmploymentType employmentType,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            List<String> skills,
            JobStatus status
    ) {
        return (Root<JobPosting> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always exclude deleted job postings
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            } else {
                predicates.add(cb.equal(root.get("status"), JobStatus.OPEN));
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), pattern);
                Predicate descMatch = cb.like(cb.lower(root.get("description")), pattern);
                Predicate companyMatch = cb.like(cb.lower(root.get("companyName")), pattern);
                predicates.add(cb.or(titleMatch, descMatch, companyMatch));
            }

            if (companyName != null && !companyName.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("companyName")), companyName.trim().toLowerCase()));
            }

            if (location != null && !location.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.trim().toLowerCase() + "%"));
            }

            if (employmentType != null) {
                predicates.add(cb.equal(root.get("employmentType"), employmentType));
            }

            if (minSalary != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxSalary"), minSalary));
            }

            if (maxSalary != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("minSalary"), maxSalary));
            }

            if (skills != null && !skills.isEmpty()) {
                Join<JobPosting, String> skillJoin = root.join("requiredSkills", JoinType.LEFT);
                List<Predicate> skillPredicates = new ArrayList<>();
                for (String s : skills) {
                    if (s != null && !s.trim().isEmpty()) {
                        skillPredicates.add(cb.equal(cb.lower(skillJoin), s.trim().toLowerCase()));
                    }
                }
                if (!skillPredicates.isEmpty()) {
                    predicates.add(cb.or(skillPredicates.toArray(new Predicate[0])));
                }
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
