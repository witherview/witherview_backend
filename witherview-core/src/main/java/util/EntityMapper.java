package util;

public interface EntityMapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
}
