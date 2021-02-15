package com.witherview.utils;

public interface EntityMapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
}
