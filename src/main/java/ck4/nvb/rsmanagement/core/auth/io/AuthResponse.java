package ck4.nvb.rsmanagement.core.auth.io;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse extends Dto {
    private String email;
    private String token;
}
