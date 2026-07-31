package com.careeros.profile.service;

import com.careeros.profile.dto.request.ProfileCreateRequest;
import com.careeros.profile.dto.request.ProfileUpdateRequest;
import com.careeros.profile.dto.response.ProfileResponse;
import com.careeros.profile.entity.Profile;
import com.careeros.profile.exception.OptimisticLockingException;
import com.careeros.profile.exception.ResourceNotFoundException;
import com.careeros.profile.mapper.ProfileMapper;
import com.careeros.profile.repository.ProfileRepository;
import com.careeros.profile.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private UUID userId;
    private UUID profileId;
    private Profile profile;
    private ProfileResponse profileResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profileId = UUID.randomUUID();

        profile = Profile.builder()
                .id(profileId)
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .headline("Senior Software Architect")
                .bio("Building cloud native scalable apps")
                .location("San Francisco, CA")
                .build();
        profile.setVersion(0L);

        profileResponse = ProfileResponse.builder()
                .id(profileId)
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .headline("Senior Software Architect")
                .version(0L)
                .completionPercentage(70)
                .build();
    }

    @Test
    @DisplayName("Should create user profile successfully")
    void createProfile_Success() {
        ProfileCreateRequest request = ProfileCreateRequest.builder()
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .headline("Senior Software Architect")
                .build();

        when(profileRepository.existsByUserId(userId)).thenReturn(false);
        when(profileMapper.toEntity(request)).thenReturn(profile);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        when(profileMapper.toResponse(profile)).thenReturn(profileResponse);

        ProfileResponse result = profileService.createProfile(request);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should throw exception when profile already exists for user")
    void createProfile_AlreadyExists() {
        ProfileCreateRequest request = ProfileCreateRequest.builder()
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(profileRepository.existsByUserId(userId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> profileService.createProfile(request));
        verify(profileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve profile by ID successfully")
    void getProfileById_Success() {
        when(profileRepository.findByIdWithDetails(profileId)).thenReturn(Optional.of(profile));
        when(profileMapper.toResponse(profile)).thenReturn(profileResponse);

        ProfileResponse result = profileService.getProfileById(profileId);

        assertNotNull(result);
        assertEquals(profileId, result.getId());
        verify(profileRepository).findByIdWithDetails(profileId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when profile ID not found")
    void getProfileById_NotFound() {
        when(profileRepository.findByIdWithDetails(profileId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profileService.getProfileById(profileId));
    }

    @Test
    @DisplayName("Should throw OptimisticLockingException on version mismatch")
    void updateProfile_OptimisticLockingFailure() {
        ProfileUpdateRequest request = ProfileUpdateRequest.builder()
                .version(1L) // Mismatched version (existing is 0L)
                .firstName("John")
                .lastName("Updated")
                .build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        assertThrows(OptimisticLockingException.class, () -> profileService.updateProfile(profileId, request));
        verify(profileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should soft delete profile successfully")
    void deleteProfile_Success() {
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        profileService.deleteProfile(profileId);

        verify(profileRepository).delete(profile);
    }
}
