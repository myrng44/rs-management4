package ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto;

import ck4.nvb.rsmanagement.core.module.order.sale_order.service.OrderFulfillmentService;

public class OrderFulfillmentStatusDto {
    private String orderId;
    private OrderFulfillmentService.OrderFulfillmentStatus status;
    private boolean canFulfill;

    // Getters and setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public OrderFulfillmentService.OrderFulfillmentStatus getStatus() { return status; }
    public void setStatus(OrderFulfillmentService.OrderFulfillmentStatus status) { this.status = status; }

    public boolean isCanFulfill() { return canFulfill; }
    public void setCanFulfill(boolean canFulfill) { this.canFulfill = canFulfill; }
}
