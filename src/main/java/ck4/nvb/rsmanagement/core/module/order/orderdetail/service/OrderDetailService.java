package ck4.nvb.rsmanagement.core.module.order.orderdetail.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;

import java.util.List;

public interface OrderDetailService {

    /**
     * Get {@noProduct} most sold products every week
     * @param noProducts number of product to be returned
     * @return a list of products
     */
    List<ProductGetDto> getMostSoldProductsLastDay(int days, int noProducts) throws AppException;
}
