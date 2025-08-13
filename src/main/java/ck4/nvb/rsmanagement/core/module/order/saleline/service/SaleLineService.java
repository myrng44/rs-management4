package ck4.nvb.rsmanagement.core.module.order.saleline.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.order.saleline.service.dto.SaleLineGetDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;

import java.util.List;

public interface SaleLineService {

    /**
     * Get {@noProduct} most sold products every week
     * @param noProducts number of product to be returned
     * @return a list of products
     */
    List<ProductGetDto> getMostSoldProductsLastDay(int days, int noProducts) throws AppException;

    /**
     * Get all order detail by orderId
     * @param orderId       identifier of an order
     * @return              list of orderDetailDto
     */
    List<SaleLineGetDto> getDetailByOrderId(String orderId) throws AppException;
}
