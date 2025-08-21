package ck4.nvb.rsmanagement.core.module.stores.inventoryadjustment.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.application.dto.PagedAndSortedResultRequestDto;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryAdjustmentFilterInputDto implements FilterInput {
  private Long batchStockId;

  private PagedAndSortedResultRequestDto paging = new PagedAndSortedResultRequestDto();

  @Override
  public List<SearchCriteria> mapToSearchCriteria() {
    List<SearchCriteria> criteria = new ArrayList<>();
    if (batchStockId != null) {
      criteria.add(new SearchCriteria("storeId", SearchOperator.EQUALS, batchStockId.toString()));
    }
    return criteria;
  }
}
