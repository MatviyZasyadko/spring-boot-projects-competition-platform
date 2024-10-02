package com.ukma.competition.platform.shared;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {

    T save(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    void deleteById(ID id);
}
