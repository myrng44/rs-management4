package develop.circlek.core.order.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class OrderEntity extends BaseEntity<Long> {

    @Column(name = "customerId")
    Long customerId;

    @Column(name = "storeId", nullable = false)
    Long storeId;

    @Column(name = "voucherId")
    Long voucherId;

    @Column(name = "finalPrice", nullable = false)
    BigDecimal finalPrice;

    @Column(name = "note")
    String note;

    @Column(name = "paymentId", nullable = false)
    Long paymentId;

    @Builder.Default
    @Column(name = "isDeleted", nullable = false)
    Boolean isDeleted = false;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderDetailEntity> orderDetails;
}