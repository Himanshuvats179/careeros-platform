package com.careeros.profile.controller;

import com.careeros.common.dto.ApiResponse;
import com.careeros.profile.dto.request.ProfileCreateRequest;
import com.careeros.profile.dto.request.ProfileSearchCriteria;
import com.careeros.profile.dto.request.ProfileUpdateRequest;
import com.careeros.profile.dto.response.FileUploadResponse;
import com.careeros.profile.dto.response.PageResponse;
import com.careeros.profile.dto.response.ProfileResponse;
import com.careeros.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profile Management", description = "APIs for user profiles, experience, education, skills, and projects")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @Operation(summary = "Create User Profile", description = "Creates a new user profile with personal details, skills, experience, and education.")
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(@Valid @RequestBody ProfileCreateRequest request) {
        log.info("REST request to create profile for user: {}", request.getUserId());
        ProfileResponse response = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Profile created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Profile by ID", description = "Retrieves a profile by its unique UUID ID.")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileById(@PathVariable UUID id) {
        log.info("REST request to get profile by ID: {}", id);
        ProfileResponse response = profileService.getProfileById(id);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", response));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Profile by User ID", description = "Retrieves a user profile using their Auth User ID.")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileByUserId(@PathVariable UUID userId) {
        log.info("REST request to get profile for User ID: {}", userId);
        ProfileResponse response = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Profile", description = "Updates an existing profile. Requires version field for optimistic locking control.")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody ProfileUpdateRequest request) {
        log.info("REST request to update profile ID: {}", id);
        ProfileResponse response = profileService.updateProfile(id, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft Delete Profile", description = "Soft-deletes a profile without destroying audit logs.")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable UUID id) {
        log.info("REST request to delete profile ID: {}", id);
        profileService.deleteProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Profile deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search & Filter Profiles", description = "Search profiles with pagination, sorting, skill filtering, and location filtering.")
    public ResponseEntity<ApiResponse<PageResponse<ProfileResponse>>> searchProfiles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String company,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        ProfileSearchCriteria criteria = ProfileSearchCriteria.builder()
                .search(search)
                .skill(skill)
                .location(location)
                .company(company)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PageResponse<ProfileResponse> response = profileService.searchProfiles(criteria);
        return ResponseEntity.ok(ApiResponse.success("Profiles retrieved successfully", response));
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Profile Picture", description = "Uploads a profile picture (JPEG, PNG, WEBP) max 10MB.")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadAvatar(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        log.info("REST request to upload avatar for profile ID: {}", id);
        FileUploadResponse response = profileService.uploadProfilePicture(id, file);
        return ResponseEntity.ok(ApiResponse.success("Profile picture uploaded successfully", response));
    }
}
