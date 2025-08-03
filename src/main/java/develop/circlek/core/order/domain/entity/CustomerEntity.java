package develop.circlek.core.order.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class CustomerEntity extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "phone", nullable = false)
    String phone;

    @Column(name = "point", nullable = false)
    Integer point;

    @Column(name = "gender", nullable = false)
    Character gender;

    @Builder.Default
    @Column(name = "isDeleted", nullable = false)
    Boolean isDeleted = false;
}