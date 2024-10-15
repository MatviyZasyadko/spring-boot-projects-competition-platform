package com.ukma.competition.platform.shared;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public abstract class GenericServiceImpl<T extends IdentifiableEntity, ID extends Serializable, R extends JpaRepository<T, ID>>
    implements GenericService<T, ID> {

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
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(ID id) {
        if (!this.existsById(id)) {
            throw new NoSuchElementException("Record is not found while trying to delete");
        }
        repository.deleteById(id);
    }
}
