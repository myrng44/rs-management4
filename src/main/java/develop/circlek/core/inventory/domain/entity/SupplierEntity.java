package develop.circlek.core.inventory.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Entity
@Table(name = "supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class SupplierEntity extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "address")
    String address;

    @Column(name = "contact", nullable = false)
    String contact;

    @Column(name = "deleted", nullable = false)
    Boolean deleted = false;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ProductEntity> products;
}
