package ck4.nvb.rsmanagement.base.web.controller;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.CrudService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

public class CrudController<D extends EntityDto<ID>, T extends IEntity<ID>, ID extends Comparable<ID> & Serializable,
        C extends CreateInput<T>, U extends UpdateInput<T>> extends GetController<D, T, ID> {

    protected CrudController(CrudService<D, T, ID> service) {
        super(service);
    }

    public CrudService<D, T, ID> getService() {
        return (CrudService<D, T, ID>) super.getService();
    }

    @PostMapping
    public D create(@RequestBody C entity) {
        return getService().create(entity);
    }

    @PutMapping("/{id}")
    public D update(@PathVariable ID id, @RequestBody U entity) {
        return getService().update(id, entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        if (!getService().exists(id)) throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);

        getService().delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
