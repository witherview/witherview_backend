package com.witherview.video;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EncodingTask {
    private String path;
    private Integer attemptedCount;
}
