package ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "batch")
public class Batch extends FullAuditedSerialIdEntity {

    @Column(name = "import_log_id")
    private String importLogId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "initial_quantity")
    private Integer initialQuantity;

    @Column(name = "imported_price")
    private Integer importedPrice;

    @Column(name = "manufacturing_date")
    private Date manufacturingDate;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "current_quantity")
    private Integer currentQuantity;

    @Column(name = "status")
    private String status;
}
