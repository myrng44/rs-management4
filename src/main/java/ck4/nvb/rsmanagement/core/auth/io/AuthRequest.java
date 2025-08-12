package ck4.nvb.rsmanagement.core.auth.io;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest extends Dto {
    private String username;
    private String password;
}
