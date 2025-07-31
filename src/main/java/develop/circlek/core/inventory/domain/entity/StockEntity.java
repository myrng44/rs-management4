package develop.circlek.core.inventory.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class StockEntity extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "location", nullable = false)
    String location;

    @Column(name = "deleted", nullable = false)
    Boolean deleted = false;

    @OneToMany(mappedBy = "fromStock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ImportLogEntity> outgoingImports;
}
