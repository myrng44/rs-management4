package ck4.nvb.rsmanagement.base.web.controller.api.method;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.CrudService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import java.io.Serializable;

public class APICrudMethod<
        D extends EntityDto<ID>,
        T extends IEntity<ID>,
        ID extends Comparable<ID> & Serializable,
        C extends CreateInput<T>,
        U extends UpdateInput<T>>
    extends APIGetMethod<D, T, ID> {

  protected APICrudMethod(CrudService<D, T, ID> crudService) {
    super(crudService);
  }

  public CrudService<D, T, ID> getService() {
    return (CrudService<D, T, ID>) super.getService();
  }

  public APIResponse<D> create(C entity) {
    D result = getService().create(entity);
    return APIResponseBuilder.created(result, "created success!");
  }

  public APIResponse<D> update(ID id, U entity) {
    D result = getService().update(id, entity);
    return APIResponseBuilder.success(result, "updated success!");
  }

  public APIResponse<Void> delete(ID id) {
    if (!getService().exists(id))
      throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);

    getService().delete(id);
    return APIResponseBuilder.success(null, "deleted success!");
  }
}
