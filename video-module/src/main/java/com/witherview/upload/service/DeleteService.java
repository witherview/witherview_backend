package com.witherview.upload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import exception.study.NotDeletedFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteService {
  private final AmazonS3 s3Client;

  @Value("${application.bucket.videos}")
  private String bucketName;

  @Value("${cloud.aws.cdn.url}")
  private String cdnUrl;

  public void delete(String savedLocation) {
    try {
      String file = savedLocation.substring(0, savedLocation.lastIndexOf(".")).replace(cdnUrl+"video/", "");

      int idx = 0;
      while (true) {
        String key = file + idx + ".ts";

        if(!s3Client.doesObjectExist(bucketName, key)) break;
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
        idx++;
      }
      // m3u8 삭제
      s3Client.deleteObject(new DeleteObjectRequest(bucketName, file+".m3u8"));
    } catch (Exception e) {
      System.out.println(e);
      throw new NotDeletedFile();
    }
  }
}
