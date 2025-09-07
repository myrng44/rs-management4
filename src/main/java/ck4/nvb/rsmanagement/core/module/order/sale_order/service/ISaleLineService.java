package ck4.nvb.rsmanagement.core.module.order.sale_order.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleLine;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleLineGetDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.util.List;

public interface ISaleLineService
        extends FullAuditedCrudService<SaleLineGetDto, SaleLine, Long, UserGetDto, Long> {

  /**
   * Get {@noProducts} most sold products every week
   *
   * @param noProducts number of product to be returned
   * @return a list of products
   */
  List<ProductGetDto.WithSales> getMostSoldProductsLastDay(int days, int noProducts) throws AppException;

  List<ProductGetDto.WithSales> getMostSoldProductsLastDayOfAStore(int days, int noProducts, Long storeId) throws AppException;
}