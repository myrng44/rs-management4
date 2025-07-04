package ck4.nvb.rsmanagement.base.web.controller;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.CreationAuditedCrudService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.CreationAudited;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

public abstract class AuditedCrudController<D extends EntityDto<ID>, T extends IEntity<ID> & CreationAudited<UID>, ID extends Comparable<ID> & Serializable, User extends EntityDto<UID>, UID extends Comparable<UID> & Serializable,
        C extends CreateInput<T>, U extends UpdateInput<T>> extends AuditedGetController<D, T, ID, User, UID> {

    protected AuditedCrudController(CreationAuditedCrudService<D, T, ID, User, UID> service) {
        super(service);
    }

    @PostMapping
    public D create(
            Authentication auth,
            @RequestBody C entity
    ) {
        User user = extractUser(auth);
        return getService().create(entity, user);
    }

    @PutMapping("/{id}")
    public D update(
            Authentication auth,
            @PathVariable ID id,
            @RequestBody U entity
    ) {
        User user = extractUser(auth);
        return getService().update(id, entity, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            Authentication auth,
            @PathVariable ID id
    ) {
        User user = extractUser(auth);
        if (!getService().exists(id, user)) throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);

        getService().delete(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
