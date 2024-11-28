package com.ukma.competition.platform.shared;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public abstract class GenericServiceImpl<T extends IdentifiableEntity, I extends Serializable, R extends JpaRepository<T, I> & JpaSpecificationExecutor<T>>
    implements GenericService<T, I> {

    R repository;

    @Override
    public T save(T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<T> findAll(Specification<T> specifications, Pageable pageable) {
        return repository.findAll(
            specifications == null ? Specification.anyOf() : specifications,
            pageable
        );
    }

    @Override
    public Optional<T> findById(I id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(I id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(I id) {
        if (!this.existsById(id)) {
            throw new NoSuchElementException("Record is not found while trying to delete");
        }
        repository.deleteById(id);
    }
}
