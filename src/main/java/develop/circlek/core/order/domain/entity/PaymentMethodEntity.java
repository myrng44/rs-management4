package develop.circlek.core.order.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "paymentMethod")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class PaymentMethodEntity extends BaseEntity<Long> {

    @Column(name = "code", nullable = false)
    @Builder.Default
    String code = "CASH";

    @Column(name = "name", nullable = false)
    String name;

    @Builder.Default
    @Column(name = "isDeleted", nullable = false)
    Boolean isDeleted = false;
}