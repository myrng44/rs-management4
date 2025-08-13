package ck4.nvb.rsmanagement.core.module.stores.batchstock.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedGeneratedIdEntity;
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
@Table(name = "import_log")
public class BatchStock extends FullAuditedGeneratedIdEntity {

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "store_id")
    private Long toStore;

    @Column(name = "quantity_total")
    private Integer quantityTotal;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    @Column(name = "quantity_reserved")
    private Integer quantityReserved;

    @Column(name =  "version")
    private Integer version;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "status")
    private String status;
}
