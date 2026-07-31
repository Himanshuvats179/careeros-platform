import axios from 'axios';

export const awsStorageService = {
  uploadFile: async (file, presignedUrl = null) => {
    // If AWS Presigned S3 URL is provided, upload directly to AWS S3 Bucket
    if (presignedUrl) {
      console.log('Uploading directly to AWS S3 via Presigned URL...');
      await axios.put(presignedUrl, file, {
        headers: { 'Content-Type': file.type }
      });
      return presignedUrl.split('?')[0]; // Clean S3 File URL
    }

    console.log('Using standard API Gateway upload pipeline...');
    return null;
  }
};
