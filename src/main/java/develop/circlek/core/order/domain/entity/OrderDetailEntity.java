package develop.circlek.core.order.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "orderDetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailEntity extends BaseEntity<Long> {

    @Column(name = "orderId", nullable = false)
    String orderId;

    @Column(name = "productId", nullable = false)
    Long productId;

    @Column(name = "quantity", nullable = false)
    Integer quantity = 0;
}