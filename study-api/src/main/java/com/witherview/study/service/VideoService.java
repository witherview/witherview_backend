package com.witherview.study.service;

import com.witherview.study.util.EncodingTaskQueue;
import exception.video.NotSavedVideo;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

    @Value("${upload.location}")
    private String uploadLocation;

    @Value("${server.url}")
    private String serverUrl;

    public String upload(MultipartFile videoFile, String fileName) {
        String originalVideoPath = uploadLocation + fileName + ".webm";
        File newVideoFile = new File(originalVideoPath);

        try {
            FileUtils.copyInputStreamToFile(videoFile.getInputStream(), newVideoFile);
            EncodingTaskQueue.addAndRun(uploadLocation + fileName);
            return serverUrl + "videos/" + fileName + ".m3u8";
        } catch (Exception e) {
            FileUtils.deleteQuietly(newVideoFile);
            throw new NotSavedVideo();
        }
    }
}
