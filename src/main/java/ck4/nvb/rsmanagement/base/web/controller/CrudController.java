package ck4.nvb.rsmanagement.base.web.controller;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.CrudService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import java.io.Serializable;

import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import org.springframework.web.bind.annotation.*;

public class CrudController<
        D extends EntityDto<ID>,
        T extends IEntity<ID>,
        ID extends Comparable<ID> & Serializable,
        C extends CreateInput<T>,
        U extends UpdateInput<T>>
    extends GetController<D, T, ID> {

  protected CrudController(CrudService<D, T, ID> service) {
    super(service);
  }

  public CrudService<D, T, ID> getService() {
    return (CrudService<D, T, ID>) super.getService();
  }

  @PostMapping
  public APIResponse<D> create(@RequestBody C entity) {
    D result = getService().create(entity);
    return APIResponseBuilder.created(result, "created success!");
  }

  @PutMapping("/{id}")
  public APIResponse<D> update(@PathVariable ID id, @RequestBody U entity) {
    D result = getService().update(id, entity);
    return APIResponseBuilder.success(result, "updated success!");
  }

  @DeleteMapping("/{id}")
  public APIResponse<Void> delete(@PathVariable ID id) {
    if (!getService().exists(id))
      throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);

    getService().delete(id);
    return APIResponseBuilder.success(null, "deleted success!");
  }
}
