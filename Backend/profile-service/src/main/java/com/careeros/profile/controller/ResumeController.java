package com.careeros.profile.controller;

import com.careeros.common.dto.ApiResponse;
import com.careeros.profile.dto.response.FileUploadResponse;
import com.careeros.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resumes")
@Tag(name = "Resume Management", description = "APIs for Resume PDF/Docx upload, parsing, and management")
public class ResumeController {

    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

    private final ProfileService profileService;

    public ResumeController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "/upload/{profileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload User Resume", description = "Uploads a resume file (PDF, DOC, DOCX) for a given profile ID.")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadResume(
            @PathVariable UUID profileId,
            @RequestParam("file") MultipartFile file) {
        log.info("REST request to upload resume for profile ID: {}", profileId);
        FileUploadResponse response = profileService.uploadResume(profileId, file);
        return ResponseEntity.ok(ApiResponse.success("Resume uploaded successfully", response));
    }
}
