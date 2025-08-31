package ck4.nvb.rsmanagement.base.web.controller.api.method;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.CreationAuditedCrudService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.CreationAudited;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import java.io.Serializable;
import org.springframework.security.core.Authentication;

public abstract class AuditedAPICrudMethod<
        D extends EntityDto<ID>,
        T extends IEntity<ID> & CreationAudited<UID>,
        ID extends Comparable<ID> & Serializable,
        User extends EntityDto<UID>,
        UID extends Comparable<UID> & Serializable,
        C extends CreateInput<T>,
        U extends UpdateInput<T>>
    extends AuditedAPIGetMethod<D, T, ID, User, UID> {

  protected AuditedAPICrudMethod(CreationAuditedCrudService<D, T, ID, User, UID> crudService) {
    super(crudService);
  }

  public APIResponse<D> create(Authentication auth, C entity) {
    User user = extractUser(auth);
    D result = getService().create(entity, user);
    return APIResponseBuilder.created(result, "created successfully!");
  }

  public APIResponse<D> update(Authentication auth, ID id, U entity) {
    User user = extractUser(auth);
    D result = getService().update(id, entity, user);
    return APIResponseBuilder.success(result, "updated successfully!");
  }

  public APIResponse<Void> delete(Authentication auth, ID id) {
    User user = extractUser(auth);
    if (!getService().exists(id, user))
      throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);

    getService().delete(id, user);
    return APIResponseBuilder.success(null, "deleted successfully!");
  }
}
