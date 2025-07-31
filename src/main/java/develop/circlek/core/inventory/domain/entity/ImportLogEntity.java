package develop.circlek.core.inventory.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "importLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportLogEntity {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "fromStock", nullable = false)
    Long fromStockId;

    @Column(name = "toStore", nullable = false)
    Long toStoreId;

    @Column(name = "startDate", nullable = false)
    LocalDateTime startDate;

    @Column(name = "deliveryDate", nullable = false)
    LocalDateTime deliveryDate;

    @Column(name = "status", nullable = false)
    Boolean status;

    @Column(name = "createdAt", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "createdBy", nullable = false)
    Long createdBy;

    @Column(name = "updatedAt", nullable = false)
    LocalDateTime updatedAt;

    @Column(name = "updatedBy", nullable = false)
    Long updatedBy;

    @Column(name = "deleted", nullable = false)
    Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromStock", insertable = false, updatable = false)
    StockEntity fromStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toStore", insertable = false, updatable = false)
    StoreEntity toStore;

    @OneToMany(mappedBy = "importLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ImportedProductEntity> importedProducts;

    @OneToMany(mappedBy = "importLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<StoreStockEntity> storeStocks;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
