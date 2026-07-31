package com.careeros.profile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir = "./uploads";
    private long maxFileSize = 10485760; // 10MB default
    private String[] allowedResumeExtensions = {"pdf", "doc", "docx"};
    private String[] allowedAvatarExtensions = {"jpg", "jpeg", "png", "webp"};

    public FileStorageProperties() {}

    public String getUploadDir() { return uploadDir; }
    public void setUploadDir(String uploadDir) { this.uploadDir = uploadDir; }

    public long getMaxFileSize() { return maxFileSize; }
    public void setMaxFileSize(long maxFileSize) { this.maxFileSize = maxFileSize; }

    public String[] getAllowedResumeExtensions() { return allowedResumeExtensions; }
    public void setAllowedResumeExtensions(String[] allowedResumeExtensions) { this.allowedResumeExtensions = allowedResumeExtensions; }

    public String[] getAllowedAvatarExtensions() { return allowedAvatarExtensions; }
    public void setAllowedAvatarExtensions(String[] allowedAvatarExtensions) { this.allowedAvatarExtensions = allowedAvatarExtensions; }
}
