package develop.circlek.core.inventory.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = { "category", "supplier", "storeStocks", "importedProducts"})
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "sku", nullable = false, unique = true)
    String sku;

    @Column(name = "description")
    String description;

    @Column(name = "unitPrice", nullable = false, precision = 10, scale = 2)
    BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "categoryId", nullable = false)
    Long categoryId;

    @Column(name = "supplierId", nullable = false)
    Long supplierId;

    @Column(name = "deleted", nullable = false)
    Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierId", insertable = false, updatable = false)
    SupplierEntity supplier;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<StoreStockEntity> storeStocks;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ImportedProductEntity> importedProducts;
}