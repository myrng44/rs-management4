package ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchGetDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchStockGetDto {
    String getProductName();
    Integer getQtyTotal();
    Integer getQtyAvailable();
    Integer getQtyReversed();
    String getBatchCode();
    String getSupplierName();
    Integer getImportedPrice();
    java.time.LocalDateTime getManufactureDate();
    java.time.LocalDateTime getExpiryDate();
}