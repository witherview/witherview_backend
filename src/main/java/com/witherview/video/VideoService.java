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

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Value("${server.url}")
    private String serverUrl;

    public String upload(MultipartFile videoFile, String fileName) {
        String originalVideoPath = uploadLocation + fileName + ".webm";
        String convertedVideoPath = uploadLocation + fileName + ".m3u8";

        File newVideoFile = new File(originalVideoPath);

        try {
            FileUtils.copyInputStreamToFile(videoFile.getInputStream(), newVideoFile);
            convertToHls(originalVideoPath, convertedVideoPath);
            return serverUrl + "videos/" + fileName + ".m3u8";
        } catch (Exception e) {
            FileUtils.deleteQuietly(newVideoFile);
            throw new NotSavedVideo();
        }
    }

    private void convertToHls(String inputPath, String outputPath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(ffmpegPath+"ffmpeg");		// ffmpeg 파일 경로
        FFprobe ffprobe = new FFprobe(ffmpegPath+"ffprobe");	// ffprobe 파일 경로
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(outputPath)
                .addExtraArgs("-c:v", "libx264")
                .addExtraArgs("-c:a", "copy")
                .addExtraArgs("-start_number", "0")
                .addExtraArgs("-hls_list_size", "0")
                .addExtraArgs("-hls_time", "10")
                .addExtraArgs("-hls_flags", "omit_endlist")
                .addExtraArgs("-hls_flags", "delete_segments")
                .addExtraArgs("-hls_flags", "append_list")
                .addExtraArgs("-f", "hls")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
}
