package com.careeros.profile.repository;

import com.careeros.profile.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByProfileId(UUID profileId);

}