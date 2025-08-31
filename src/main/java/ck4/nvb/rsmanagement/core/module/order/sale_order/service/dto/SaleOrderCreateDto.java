package ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrderCreateDto extends BaseSaleOrderDto implements CreateInput<SaleOrder> {

/*  @JsonSerialize(using = ToStringSerializer.class)
  private Long storeId;*/

  @NotEmpty private List<SaleLineDto> lines;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long voucherId;

  private Long paymentId;

  @Override
  public SaleOrder mapToEntity() {

    SaleOrder saleOrder = new SaleOrder();
    saleOrder.setCustomerId(getCustomerId());
    saleOrder.setNote(getNote());
    saleOrder.setVoucherId(voucherId);
    saleOrder.setPaymentId(paymentId);
    return saleOrder;
  }
}
