package ck4.nvb.rsmanagement.core.auth.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends EntityDto<Long> {
    private String username;

    private String fullName;

    private String email;

    private String phone;

    private String storeId;
}
