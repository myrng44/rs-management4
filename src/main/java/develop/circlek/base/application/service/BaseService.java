package develop.circlek.base.application.service;

import develop.circlek.base.domain.entity.BaseEntity;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.base.application.dto.BaseDTO;
import develop.circlek.base.application.exception.NotFoundException;
import develop.circlek.core.auth.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import develop.circlek.core.auth.application.service.AuthService;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T extends BaseEntity<ID>, D extends BaseDTO, ID extends Serializable> {
    
    protected abstract BaseRepository<T, ID> getRepository();
    protected abstract D convertToDTO(T entity);
    protected abstract T convertToEntity(D dto);
    protected abstract void updateEntityFromDTO(T entity, D dto);
    @Autowired
    protected AuthService authService;

    public D findById(ID id) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found with id: " + id));
        return convertToDTO(entity);
    }
    
    public List<D> findAll() {
        return getRepository().findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    public Page<D> findAll(Pageable pageable) {
        return getRepository().findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public D save(D dto) {
        T entity = convertToEntity(dto);
        T savedEntity = getRepository().save(entity);
        return convertToDTO(savedEntity);
    }
    
    public D update(ID id, D dto) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found with id: " + id));
        updateEntityFromDTO(entity, dto);
        T updatedEntity = getRepository().save(entity);
        return convertToDTO(updatedEntity);
    }
    
    public void deleteById(ID id) {
        if (!getRepository().existsById(id)) {
            throw new NotFoundException("Entity not found with id: " + id);
        }
        getRepository().deleteById(id);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            return authService.getUserIdByUsername(username);
        }
        return null;
    }

    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }
}