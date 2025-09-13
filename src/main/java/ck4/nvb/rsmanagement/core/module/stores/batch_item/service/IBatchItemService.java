package ck4.nvb.rsmanagement.core.module.stores.batch_item.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.domain.BatchItem;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto.BatchItemDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface IBatchItemService extends FullAuditedCrudService<BatchItemDto, BatchItem, Long, UserGetDto, Long> {
}
