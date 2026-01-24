package org.example.scrum.service.base;

import org.example.scrum.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract String getEntityName();

    protected T findEntityById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), (Long) id));
    }

    protected void validateEntityExists(ID id) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException(getEntityName(), (Long) id);
        }
    }
}

