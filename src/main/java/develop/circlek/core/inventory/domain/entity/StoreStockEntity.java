package develop.circlek.core.inventory.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "storeStocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class StoreStockEntity extends BaseEntity<Long> {

    @Column(name = "productId", nullable = false)
    Long productId;

    @Column(name = "storeId", nullable = false)
    Long storeId;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "importId", nullable = false)
    String importId;

    @Column(name = "minQuantity", nullable = false)
    Integer minQuantity;

    @Column(name = "deleted", nullable = false)
    Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", insertable = false, updatable = false)
    StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importId", insertable = false, updatable = false)
    ImportLogEntity importLog;
}