package ck4.nvb.rsmanagement.core.module.users.permission.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.Permission;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class PermissionDto extends EntityDto<Long>
    implements CreateInput<Permission>, UpdateInput<Permission> {
  private String code;
  private String desc;

  @Override
  public Permission mapToEntity() {
    return new ModelMapper().map(this, Permission.class);
  }

  @Override
  public boolean mapToEntity(Permission entity) {
    return false;
  }
}
