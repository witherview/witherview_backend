package com.witherview.video;

import com.sun.management.OperatingSystemMXBean;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EncodingTaskQueue {

    private static Integer CONFIG_CPU_LOAD_PERCENTAGE = 10;
    private static Integer CONFIG_THREAD_WAIT_SECONDS = 1000;
    private static String ffmpegPath;

    @Value("${task.queue.cpu.limit-percentage}")
    public void setConfigCpuLoadPercentage(Integer p) {
        CONFIG_CPU_LOAD_PERCENTAGE = p;
    }

    @Value("${task.queue.thread.wait-milliseconds}")
    public void setConfigThreadWaitSeconds(Integer s) {
        CONFIG_THREAD_WAIT_SECONDS = s;
    }

    @Value("${ffmpeg.path}")
    public void setFfmpegPath(String path) {
        ffmpegPath = path;
    }

    private static Queue<String> queue = new LinkedList<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);
    public static void addAndRun(String path) {
        queue.add(path);
        run();
    }

    private static boolean canProgress() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        return osBean.getSystemCpuLoad() * 100 >= CONFIG_CPU_LOAD_PERCENTAGE;
    }

    private static void encode(String path) throws IOException {
        String inputPath = path + ".webm";
        String outputPath = path + ".m3u8";

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath + "ffmpeg");		// ffmpeg 파일 경로
        FFprobe ffprobe = new FFprobe(ffmpegPath + "ffprobe");	// ffprobe 파일 경로
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(outputPath)
                .addExtraArgs("-c:v", "libx264")
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

    private static void run() {
        executorService.submit(() -> {
            if (canProgress()) {
                try {
                    Thread.sleep(CONFIG_THREAD_WAIT_SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                run();
                return;
            }
            String path = queue.poll();
            if (path != null) {
                try {
                    encode(path);
                } catch (IOException e) {
                    queue.add(path);
                    run();
                }
            }
        });
    }
}
