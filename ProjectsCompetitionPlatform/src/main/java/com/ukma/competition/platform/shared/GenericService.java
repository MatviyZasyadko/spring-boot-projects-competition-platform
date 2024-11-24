package com.ukma.competition.platform.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {

    T save(T entity);

    List<T> findAll();

    Page<T> findAll(Specification<T> specifications, Pageable pageable);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    void deleteById(ID id);
}
