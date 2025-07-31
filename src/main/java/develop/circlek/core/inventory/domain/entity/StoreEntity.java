package develop.circlek.core.inventory.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "store")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class StoreEntity extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "phone", nullable = false, unique = true)
    String phone;

    @Column(name = "isActive", nullable = false)
    Boolean isActive = true;

    @Column(name = "deleted", nullable = false)
    Boolean deleted = false;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<StoreStockEntity> storeStocks;

    @OneToMany(mappedBy = "toStore", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ImportLogEntity> incomingImports;
}