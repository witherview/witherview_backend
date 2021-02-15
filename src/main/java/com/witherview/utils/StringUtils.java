package com.witherview.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtils {
    private static final String TIME_FORMATTER= "HH:mm:ss";
    private static final String DATETIME_FORMATTER = "YYYY-MM-dd'T'HH:mm:ss";

    public static String getCurrentDateTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMATTER);
        return LocalDateTime.now().format(formatter);
    }
    public static String getTimeStamp(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMATTER);
        return time.format(formatter);
    }

}
