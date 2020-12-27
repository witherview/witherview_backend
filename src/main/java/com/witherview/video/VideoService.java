package com.witherview.video;

import com.witherview.video.exception.NotSavedVideo;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
