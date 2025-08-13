package ck4.nvb.rsmanagement.core.module.order.orderdetail.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetail;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto.OrderDetailGetDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

import java.util.List;

public interface IOrderDetailService extends FullAuditedCrudService<OrderDetailGetDto, OrderDetail, Long, UserGetDto, Long> {

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
    List<OrderDetailGetDto> getDetailByOrderId(String orderId) throws AppException;
}
