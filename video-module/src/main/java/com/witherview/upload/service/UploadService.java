package com.witherview.upload.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.util.IOUtils;
import exception.video.NotSavedVideo;
import java.io.ByteArrayInputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {

  private final TransferManager transferManager;

  @Value("${application.bucket.videos}")
  private String bucketName;

  @Value("${cloud.aws.cdn.url}")
  private String cdnUrl;

//  @Async
  public String upload(String userId, MultipartFile videoFile) {
    try {
      byte[] bytes = IOUtils.toByteArray(videoFile.getInputStream());
      ObjectMetadata metadata =new ObjectMetadata();
      metadata.setContentLength(bytes.length);
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

      var videoName = makeVideoName(userId, videoFile.getOriginalFilename());
      var request = new PutObjectRequest(bucketName, videoName, byteArrayInputStream, metadata);

      // 저장
      transferManager.upload(request);
      return getSavedLocation(videoName);
    } catch (Exception e) {
      throw new NotSavedVideo();
    }
  }

  private String makeVideoName(String userId, String originName) {
    String uuid = UUID.randomUUID().toString().substring(0, 10);
    String fileExtension = originName.substring(originName.lastIndexOf("."));

    originName = originName.substring(0, originName.lastIndexOf("."))
                          .replace(" ", "")
                          .replace(".", "")
                          .replace("_", "");

    return userId + "_" + originName + "_" + uuid + "_" + fileExtension;
  }

  private String getSavedLocation(String videoName) {
    videoName = videoName.substring(0, videoName.lastIndexOf("."));
    return cdnUrl + "video/" + videoName + ".m3u8";
  }
}