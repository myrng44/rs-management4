package ck4.nvb.rsmanagement.core.module.stores.importedproduct.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface IBatchService extends FullAuditedCrudService<BatchDto, Batch, Long, UserGetDto, Long> {
}
