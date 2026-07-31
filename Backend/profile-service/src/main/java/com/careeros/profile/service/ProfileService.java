package com.careeros.profile.service;

import com.careeros.profile.dto.request.ProfileCreateRequest;
import com.careeros.profile.dto.request.ProfileSearchCriteria;
import com.careeros.profile.dto.request.ProfileUpdateRequest;
import com.careeros.profile.dto.response.FileUploadResponse;
import com.careeros.profile.dto.response.PageResponse;
import com.careeros.profile.dto.response.ProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProfileService {
    ProfileResponse createProfile(ProfileCreateRequest request);
    ProfileResponse getProfileById(UUID id);
    ProfileResponse getProfileByUserId(UUID userId);
    ProfileResponse updateProfile(UUID id, ProfileUpdateRequest request);
    void deleteProfile(UUID id);
    PageResponse<ProfileResponse> searchProfiles(ProfileSearchCriteria criteria);
    FileUploadResponse uploadResume(UUID profileId, MultipartFile file);
    FileUploadResponse uploadProfilePicture(UUID profileId, MultipartFile file);
}
