package ck4.nvb.rsmanagement.core.module.stores.batch.service.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchItemCreateDto {
  @JsonSerialize(using = ToStringSerializer.class)
  private Long productId;
  @JsonSerialize(using = ToStringSerializer.class)
  private Long supplierId;
  private Integer qty;
  private Integer importPrice;
  private LocalDateTime manufactureDate;
  private LocalDateTime expiryDate;
}
