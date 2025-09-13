package ck4.nvb.rsmanagement.core.module.stores.batch.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.BatchRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchGetDto;
import ck4.nvb.rsmanagement.core.module.stores.supplier.domain.Supplier;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.ISupplierService;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.dto.SupplierDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("batchService")
public class BatchServiceImpl
    extends FullAuditedCrudServiceImpl<BatchGetDto, Batch, Long, UserGetDto, Long>
    implements IBatchService {

  protected BatchServiceImpl(BatchRepository repository) {
    super(repository, Batch.class);
  }

  @Autowired private ModelMapper modelMapper;
  @Autowired private ISupplierService supplierService;

  @Override
  public BatchRepository getRepository() {
    return (BatchRepository) super.getRepository();
  }

  @Override
  public BatchGetDto mapToEntityDto(Batch entity) {
/*    SupplierDto supplier = supplierService.get(entity.getSupplierId());
    BatchGetDto batchGetDto = new BatchGetDto();
    batchGetDto.setId(entity.getId());
    batchGetDto.setBatchCode(entity.getBatchCode());
    batchGetDto.setSupplierName(supplier.getName());
    batchGetDto.setImportedPrice(entity.getImportedPrice());
    batchGetDto.setManufactureDate(entity.getManufactureDate());
    batchGetDto.setExpiryDate(entity.getExpiryDate());
    return batchGetDto;*/
    return modelMapper.map(entity, BatchGetDto.class);
  }

  @Override
  public Map<String, List<SearchOperator>> getSearchableKeys() {
    Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
    return keys;
  }

  @Override
  public Set<String> getSortableKeys() {
    Set<String> keys = super.getSortableKeys();
    return keys;
  }
}
