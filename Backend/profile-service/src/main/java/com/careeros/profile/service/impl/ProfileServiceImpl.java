package com.careeros.profile.service.impl;

import com.careeros.profile.dto.request.ProfileCreateRequest;
import com.careeros.profile.dto.request.ProfileSearchCriteria;
import com.careeros.profile.dto.request.ProfileUpdateRequest;
import com.careeros.profile.dto.response.FileUploadResponse;
import com.careeros.profile.dto.response.PageResponse;
import com.careeros.profile.dto.response.ProfileResponse;
import com.careeros.profile.entity.Profile;
import com.careeros.profile.exception.OptimisticLockingException;
import com.careeros.profile.exception.ResourceNotFoundException;
import com.careeros.profile.mapper.ProfileMapper;
import com.careeros.profile.repository.ProfileRepository;
import com.careeros.profile.service.FileStorageService;
import com.careeros.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final FileStorageService fileStorageService;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper, FileStorageService fileStorageService) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public ProfileResponse createProfile(ProfileCreateRequest request) {
        log.info("Creating user profile for userId: {}", request.getUserId());
        if (profileRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("Profile already exists for user ID: " + request.getUserId());
        }

        Profile profile = profileMapper.toEntity(request);
        Profile savedProfile = profileRepository.save(profile);

        log.info("Profile successfully created with ID: {}", savedProfile.getId());
        return profileMapper.toResponse(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "profiles", key = "#id")
    public ProfileResponse getProfileById(UUID id) {
        log.info("Fetching profile from database for ID: {}", id);
        Profile profile = profileRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + id));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles", key = "#userId")
    public ProfileResponse getProfileByUserId(UUID userId) {
        log.info("Fetching profile for user ID: {}", userId);
        Profile profile = profileRepository.findByUserIdWithDetails(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for User ID: " + userId));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"profiles", "user_profiles"}, allEntries = true)
    public ProfileResponse updateProfile(UUID id, ProfileUpdateRequest request) {
        log.info("Updating profile with ID: {}", id);
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + id));

        if (!existingProfile.getVersion().equals(request.getVersion())) {
            throw new OptimisticLockingException("Profile version conflict. Current version: " + existingProfile.getVersion() + ", Provided version: " + request.getVersion());
        }

        profileMapper.updateEntityFromRequest(existingProfile, request);
        Profile updatedProfile = profileRepository.save(existingProfile);

        log.info("Successfully updated profile ID: {}", id);
        return profileMapper.toResponse(updatedProfile);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"profiles", "user_profiles"}, allEntries = true)
    public void deleteProfile(UUID id) {
        log.info("Performing soft-delete for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + id));
        profileRepository.delete(profile);
        log.info("Profile ID {} soft-deleted successfully", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProfileResponse> searchProfiles(ProfileSearchCriteria criteria) {
        Sort sort = Sort.by(
                Sort.Direction.fromString(criteria.getSortDirection()),
                criteria.getSortBy()
        );
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Page<Profile> profilesPage = profileRepository.searchProfiles(
                criteria.getSearch(),
                criteria.getSkill(),
                criteria.getLocation(),
                criteria.getCompany(),
                pageable
        );

        Page<ProfileResponse> responsePage = profilesPage.map(profileMapper::toResponse);
        return PageResponse.fromPage(responsePage);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"profiles", "user_profiles"}, allEntries = true)
    public FileUploadResponse uploadResume(UUID profileId, MultipartFile file) {
        log.info("Uploading resume for profile ID: {}", profileId);
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));

        if (profile.getResumeUrl() != null) {
            fileStorageService.deleteFile(profile.getResumeUrl());
        }

        FileUploadResponse uploadResponse = fileStorageService.storeResume(profile.getUserId(), file);
        profile.setResumeUrl(uploadResponse.getFileUrl());
        profileRepository.save(profile);

        log.info("Resume stored successfully for profile ID: {}", profileId);
        return uploadResponse;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"profiles", "user_profiles"}, allEntries = true)
    public FileUploadResponse uploadProfilePicture(UUID profileId, MultipartFile file) {
        log.info("Uploading profile picture for profile ID: {}", profileId);
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));

        if (profile.getProfilePictureUrl() != null) {
            fileStorageService.deleteFile(profile.getProfilePictureUrl());
        }

        FileUploadResponse uploadResponse = fileStorageService.storeProfilePicture(profile.getUserId(), file);
        profile.setProfilePictureUrl(uploadResponse.getFileUrl());
        profileRepository.save(profile);

        log.info("Profile picture stored successfully for profile ID: {}", profileId);
        return uploadResponse;
    }
}
