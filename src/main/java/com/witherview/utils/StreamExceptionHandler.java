package com.witherview.utils;

public interface StreamExceptionHandler<T, R> {
    R apply(T r) throws Exception;
}
