package com.careeros.profile.service;

import com.careeros.profile.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorageService {
    FileUploadResponse storeResume(UUID userId, MultipartFile file);
    FileUploadResponse storeProfilePicture(UUID userId, MultipartFile file);
    void deleteFile(String fileUrl);
}
