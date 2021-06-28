//package com.witherview.study.util;
//
//import com.sun.management.OperatingSystemMXBean;
//import java.io.IOException;
//import java.lang.management.ManagementFactory;
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import net.bramp.ffmpeg.FFmpeg;
//import net.bramp.ffmpeg.FFmpegExecutor;
//import net.bramp.ffmpeg.FFprobe;
//import net.bramp.ffmpeg.builder.FFmpegBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EncodingTaskQueue {
//    private static Integer CONFIG_CPU_LOAD_PERCENTAGE;
//    private static Integer CONFIG_THREAD_WAIT_SECONDS;
//    private static Integer ENCODING_MAXIMUM_ATTEMPT_COUNT;
//    private static String FFMPEG_PATH;
//
//    @Value("${task.queue.maximum-attempt-count}")
//    public void setConfigMaximumAttemptCount(Integer c) {
//        ENCODING_MAXIMUM_ATTEMPT_COUNT = c;
//    }
//    @Value("${task.queue.cpu.limit-percentage}")
//    public void setConfigCpuLoadPercentage(Integer p) {
//        CONFIG_CPU_LOAD_PERCENTAGE = p;
//    }
//    @Value("${task.queue.thread.wait-milliseconds}")
//    public void setConfigThreadWaitSeconds(Integer s) {
//        CONFIG_THREAD_WAIT_SECONDS = s;
//    }
//    @Value("${ffmpeg.path}")
//    public void setFfmpegPath(String p) {
//        FFMPEG_PATH = p;
//    }
//
//    private static Queue<EncodingTask> queue = new LinkedList<>();
//    private static ExecutorService executorService = Executors.newFixedThreadPool(1);
//    public static void addAndRun(String path) {
//        queue.add(new EncodingTask(path, 1));
//        run();
//    }
//
//    private static boolean canProgress() {
//        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
//        return osBean.getSystemCpuLoad() * 100 >= CONFIG_CPU_LOAD_PERCENTAGE;
//    }
//
//    private static void encode(String path) throws IOException {
//        String inputPath = path + ".webm";
//        String outputPath = path + ".m3u8";
//        FFmpeg ffmpeg = new FFmpeg(FFMPEG_PATH + "ffmpeg");		// ffmpeg 파일 경로
//        FFprobe ffprobe = new FFprobe(FFMPEG_PATH + "ffprobe");	// ffprobe 파일 경로
//        FFmpegBuilder builder = new FFmpegBuilder()
//                .setInput(inputPath)
//                .addOutput(outputPath)
//                .addExtraArgs("-c:v", "libx264")
//                .addExtraArgs("-start_number", "0")
//                .addExtraArgs("-hls_list_size", "0")
//                .addExtraArgs("-hls_time", "10")
//                .addExtraArgs("-hls_flags", "omit_endlist")
//                .addExtraArgs("-hls_flags", "delete_segments")
//                .addExtraArgs("-hls_flags", "append_list")
//                .addExtraArgs("-f", "hls")
//                .done();
//
//        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
//        executor.createJob(builder).run();
//    }
//
//    private static void sleep() {
//        try {
//            Thread.sleep(CONFIG_THREAD_WAIT_SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void run() {
//        executorService.submit(() -> {
//            if (canProgress()) {
//                sleep();
//                run();
//                return;
//            }
//            goEncode();
//        });
//    }
//
//    private static void goEncode() {
//        EncodingTask encodingTask = queue.poll();
//        if (encodingTask != null && encodingTask.getAttemptedCount() <= ENCODING_MAXIMUM_ATTEMPT_COUNT) {
//            try {
//                encode(encodingTask.getPath());
//            } catch (IOException e) {
//                queue.add(new EncodingTask(encodingTask.getPath(), encodingTask.getAttemptedCount() + 1));
//                sleep();
//                run();
//            }
//        }
//    }
//}
