package develop.circlek.core.order.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class VoucherEntity extends BaseEntity<Long> {

    @Column(name = "code", nullable = false)
    String code;

    @Column(name = "description")
    String description;

    @Column(name = "discountPer")
    Integer discountPercent = 0;

    @Column(name = "discountVal")
    Integer discountValue;

    @Column(name = "validFrom", nullable = false)
    LocalDate startTime;

    @Column(name = "validTo", nullable = false)
    LocalDate expirationTime;

    @Builder.Default
    @Column(name = "isActive", nullable = false)
    Boolean isActive = true;

    @Builder.Default
    @Column(name = "isDeleted", nullable = false)
    Boolean isDeleted = false;
}