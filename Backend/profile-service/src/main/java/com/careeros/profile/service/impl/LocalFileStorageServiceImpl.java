package com.careeros.profile.service.impl;

import com.careeros.profile.config.FileStorageProperties;
import com.careeros.profile.dto.response.FileUploadResponse;
import com.careeros.profile.exception.FileStorageException;
import com.careeros.profile.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorageServiceImpl.class);

    private final Path fileStorageLocation;
    private final FileStorageProperties properties;

    public LocalFileStorageServiceImpl(FileStorageProperties properties) {
        this.properties = properties;
        this.fileStorageLocation = Paths.get(properties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the upload directory: " + this.fileStorageLocation, ex);
        }
    }

    @Override
    public FileUploadResponse storeResume(UUID userId, MultipartFile file) {
        validateFile(file, properties.getAllowedResumeExtensions());
        String fileName = generateFileName(userId, "resume", file.getOriginalFilename());
        return saveFile(file, fileName, "resumes");
    }

    @Override
    public FileUploadResponse storeProfilePicture(UUID userId, MultipartFile file) {
        validateFile(file, properties.getAllowedAvatarExtensions());
        String fileName = generateFileName(userId, "avatar", file.getOriginalFilename());
        return saveFile(file, fileName, "avatars");
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;
        try {
            Path filePath = this.fileStorageLocation.resolve(Paths.get(fileUrl)).normalize();
            Files.deleteIfExists(filePath);
            log.info("Successfully deleted file at path: {}", filePath);
        } catch (IOException e) {
            log.warn("Failed to delete file at {}: {}", fileUrl, e.getMessage());
        }
    }

    private void validateFile(MultipartFile file, String[] allowedExtensions) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file.");
        }
        if (file.getSize() > properties.getMaxFileSize()) {
            throw new FileStorageException("File size exceeds maximum limit of " + (properties.getMaxFileSize() / (1024 * 1024)) + "MB");
        }
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getExtension(originalFilename).toLowerCase();

        boolean allowed = Arrays.stream(allowedExtensions)
                .anyMatch(ext -> ext.equalsIgnoreCase(fileExtension));

        if (!allowed) {
            throw new FileStorageException("Invalid file extension: ." + fileExtension + ". Allowed extensions: " + Arrays.toString(allowedExtensions));
        }
    }

    private String generateFileName(UUID userId, String type, String originalFilename) {
        String extension = getExtension(originalFilename);
        return userId + "_" + type + "_" + System.currentTimeMillis() + "." + extension;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    private FileUploadResponse saveFile(MultipartFile file, String fileName, String subDir) {
        try {
            Path targetDir = this.fileStorageLocation.resolve(subDir);
            Files.createDirectories(targetDir);

            Path targetLocation = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String relativeUrl = "/" + subDir + "/" + fileName;

            return FileUploadResponse.builder()
                    .fileName(fileName)
                    .fileUrl(relativeUrl)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
