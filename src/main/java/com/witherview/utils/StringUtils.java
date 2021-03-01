package com.witherview.utils;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringUtils {
    private static final DateTimeFormatter TO_DATE_FORMATTER =  DateTimeFormatter.ofPattern("YYYY-MM-dd");
    private static final DateTimeFormatter TO_TIME_FORMATTER =  DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String TIME_FORMATTER= "HH:mm:ss";
    private static final String DATETIME_FORMATTER = "YYYY-MM-dd'T'HH:mm:ss";

    public static String getCurrentDateTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMATTER);
        return LocalDateTime.now().format(formatter);
    }
    public static String toTimeStamp(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMATTER);
        return time.format(formatter);
    }
    public static LocalDate toLocalDate(String time) {
        return LocalDate.parse(time, TO_DATE_FORMATTER);
    }
    public static LocalTime toLocalTime(String time) {
        return LocalTime.parse(time, TO_TIME_FORMATTER);
    }
}
