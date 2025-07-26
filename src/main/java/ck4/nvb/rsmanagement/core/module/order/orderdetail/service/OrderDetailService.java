package ck4.nvb.rsmanagement.core.module.order.orderdetail.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;

import java.util.List;

public interface OrderDetailService {

    /**
     * Get {@noProduct} most sold products every week
     * @param noProducts number of product to be returned
     * @return a list of products
     */
    List<Product> getMostSoldProductsPerWeek(int noProducts) throws AppException;
}
