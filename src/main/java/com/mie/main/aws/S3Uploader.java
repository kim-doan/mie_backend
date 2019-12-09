package com.mie.main.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }
    
    public void addDirectory(String dirName) {
    	amazonS3Client.putObject(bucket, dirName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
    
    public void removeFolder(String dirName) {
    	if (amazonS3Client.doesBucketExist(bucket)) {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucket)
                    .withPrefix(dirName);

            ObjectListing objectListing = amazonS3Client.listObjects(listObjectsRequest);

            while (true) {
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                	amazonS3Client.deleteObject(bucket, objectSummary.getKey());
                }
                if (objectListing.isTruncated()) {
                    objectListing = amazonS3Client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
    
    public List<String> getObjectslistFromFolder(String folderKey) {
    	   
    	  ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
    	                                      .withBucketName(bucket)
    	                                      .withPrefix(folderKey + "/");
    	 
    	  List<String> keys = new ArrayList<>();
    	 
    	  ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);
    	  for (;;) {
    	    List<S3ObjectSummary> summaries = objects.getObjectSummaries();
    	    if (summaries.size() < 1) {
    	      break;
    	    }
    	    summaries.forEach(s -> keys.add(s.getKey()));
    	    objects = amazonS3Client.listNextBatchOfObjects(objects);
    	  }
    	 
    	  return keys;
    	}
}
