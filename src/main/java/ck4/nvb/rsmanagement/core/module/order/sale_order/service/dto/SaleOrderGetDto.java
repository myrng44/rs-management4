package ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrderGetDto extends EntityDto<String> {
  @JsonSerialize(using = ToStringSerializer.class)
  private Long customerId;

  private String customerName;

  private List<SaleLineGetDto> saleLines;

  private Long storeId;

  private String voucherCode;

  private Integer finalPrice;

  private String note;

  private String paymentMethodName;
}
