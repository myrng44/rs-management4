package develop.circlek.core.inventory.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "importedProducts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ImportedProductEntity extends BaseEntity<Long> {

    @Column(name = "importLogId", nullable = false)
    String importLogId;

    @Column(name = "productId", nullable = false)
    Long productId;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "importPrice", nullable = false)
    Integer importPrice;

    @Column(name = "manufacturingDate", nullable = false)
    LocalDateTime manufacturingDate;

    @Column(name = "expiryDate", nullable = false)
    LocalDateTime expiryDate;

    @Column(name = "initialQuantity", nullable = false)
    Short initialQuantity;

    @Column(name = "currentQuantity", nullable = false)
    Short currentQuantity;

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "deleted", nullable = false)
    Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importLogId", insertable = false, updatable = false)
    ImportLogEntity importLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    ProductEntity product;
}