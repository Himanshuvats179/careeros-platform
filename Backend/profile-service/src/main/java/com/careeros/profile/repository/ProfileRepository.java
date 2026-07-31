package com.careeros.profile.repository;

import com.careeros.profile.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID>, JpaSpecificationExecutor<Profile> {

    Optional<Profile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.skills LEFT JOIN FETCH p.experiences WHERE p.id = :id")
    Optional<Profile> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.skills LEFT JOIN FETCH p.experiences WHERE p.userId = :userId")
    Optional<Profile> findByUserIdWithDetails(@Param("userId") UUID userId);

    @Query("""
        SELECT DISTINCT p FROM Profile p 
        LEFT JOIN p.skills s 
        LEFT JOIN p.experiences e 
        WHERE (:search IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) 
               OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')) 
               OR LOWER(p.headline) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.location) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:skill IS NULL OR LOWER(s.name) = LOWER(:skill))
          AND (:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')))
          AND (:company IS NULL OR LOWER(e.companyName) LIKE LOWER(CONCAT('%', :company, '%')))
    """)
    Page<Profile> searchProfiles(
            @Param("search") String search,
            @Param("skill") String skill,
            @Param("location") String location,
            @Param("company") String company,
            Pageable pageable
    );
}