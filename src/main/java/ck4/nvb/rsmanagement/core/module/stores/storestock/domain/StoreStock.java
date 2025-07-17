package ck4.nvb.rsmanagement.core.module.stores.storestock.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "store_stock")
public class StoreStock extends FullAuditedSerialIdEntity {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "import_log_id")
    private String importLogId;
}
