package com.ukma.competition.platform.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, I> {

    T save(T entity);

    List<T> findAll();

    Page<T> findAll(Specification<T> specifications, Pageable pageable);

    Optional<T> findById(I id);

    boolean existsById(I id);

    void deleteById(I id);
}
