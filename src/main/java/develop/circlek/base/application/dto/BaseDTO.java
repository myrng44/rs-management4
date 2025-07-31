package develop.circlek.base.application.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class BaseDTO {
    Long id;
    LocalDateTime createAt;
    Long createBy;
    LocalDateTime updateAt;
    Long updateBy;
}