package ck4.nvb.rsmanagement.core.module.stores.importlog.domain;

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
public class ImportLog extends FullAuditedGeneratedIdEntity {

    @Column(name = "from_stock")
    private Long fromStock;

    @Column(name = "to_store")
    private Long toStore;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "status")
    private String status;
}
