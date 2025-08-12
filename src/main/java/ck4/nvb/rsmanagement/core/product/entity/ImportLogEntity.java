package ck4.nvb.rsmanagement.core.product.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedGeneratedIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportLogEntity extends FullAuditedGeneratedIdEntity {
    @Column(name = "from_stock")
    private Long fromStock;

    @Column(name = "to_store")
    private Long toStore;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "status")
    private boolean status;
}
