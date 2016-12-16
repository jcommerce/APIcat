package pl.jcommerce.apicat.business.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.jcommerce.apicat.business.exception.IdDoesNotMatchException;
import pl.jcommerce.apicat.business.exception.ResourceNotFoundException;
import pl.jcommerce.apicat.model.entity.BaseEntity;

/**
 * Created by jada on 05.12.2016.
 */
public abstract class CrudService<T extends BaseEntity, K extends JpaRepository<T, Long>> {

    @Getter
    @Autowired
    protected K repository;

    public T findOneById(Long id) {
        T entity = repository.findOne(id);

        if (entity == null) {
            throw new ResourceNotFoundException();
        }

        return entity;
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public T delete(Long id) {
        T entity = repository.findOne(id);

        if (entity != null) {
            repository.delete(entity);
        } else {
            throw new ResourceNotFoundException();
        }

        return entity;
    }

    public T save(T entity) {
        repository.save(entity);

        return entity;
    }

    public T update(T entity, Long id) throws ResourceNotFoundException, IdDoesNotMatchException {
        if (repository.exists(id)) {
            if (id.equals(entity.getId())) {
                repository.save(entity);
            } else {
                throw new IdDoesNotMatchException();
            }
        } else {
            throw new ResourceNotFoundException();
        }

        return entity;
    }

    public boolean exists(Long id) {
        return repository.exists(id);
    }

}
