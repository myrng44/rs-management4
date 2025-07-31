package develop.circlek.base.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity<ID extends Serializable> {
    
    @Id
    @GeneratedValue(generator = "snowflakeGenerator")
    @GenericGenerator(name = "snowflakeGenerator", strategy = "develop.circlek.base.utils.SnowflakeIdGenerator")
    @Column(name = "id")
    ID id;

    @Column(name = "createAt")
    LocalDateTime createAt;

    @Column(name = "createBy")
    Long createBy;

    @Column(name = "updateAt")
    LocalDateTime updateAt;

    @Column(name = "updateBy")
    Long updateBy;
    
    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}