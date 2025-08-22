package ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO detail cho SaleOrder - chứa toàn bộ thông tin của 1 order , bao gồm cả chi tiết các sale line.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrderGetFullDto extends BaseSaleOrderDto {

  private String customerName;

  private Long storeId;

  private String voucherCode;

  private Integer finalPrice;

  private String paymentMethodName;

  private List<SaleLineGetDto> saleLines;
}
