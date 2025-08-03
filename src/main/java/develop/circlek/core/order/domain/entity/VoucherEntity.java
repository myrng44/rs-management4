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

    @Column(name = "desc")
    String description;

    @Column(name = "discountPer")
    @Builder.Default
    Short discountPercent = 0;

    @Column(name = "discountVal")
    @Builder.Default
    BigDecimal discountValue = BigDecimal.ZERO;

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