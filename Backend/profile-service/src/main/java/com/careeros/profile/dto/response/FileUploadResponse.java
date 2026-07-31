package com.careeros.profile.dto.response;

public class FileUploadResponse {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private long fileSize;

    public FileUploadResponse() {}

    public FileUploadResponse(String fileName, String fileUrl, String fileType, long fileSize) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public static FileUploadResponseBuilder builder() { return new FileUploadResponseBuilder(); }

    public static class FileUploadResponseBuilder {
        private String fileName;
        private String fileUrl;
        private String fileType;
        private long fileSize;

        public FileUploadResponseBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public FileUploadResponseBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public FileUploadResponseBuilder fileType(String fileType) { this.fileType = fileType; return this; }
        public FileUploadResponseBuilder fileSize(long fileSize) { this.fileSize = fileSize; return this; }

        public FileUploadResponse build() {
            return new FileUploadResponse(fileName, fileUrl, fileType, fileSize);
        }
    }
}
