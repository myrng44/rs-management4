package ck4.nvb.rsmanagement.core.web.security.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Getter @Size @NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenInput extends Dto {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String refreshToken;
}
