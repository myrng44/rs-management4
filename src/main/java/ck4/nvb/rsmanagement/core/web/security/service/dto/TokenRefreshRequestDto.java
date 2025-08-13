package ck4.nvb.rsmanagement.core.web.security.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TokenRefreshRequestDto extends RefreshTokenInput {

    private String ipAddress;

    private String deviceSession;
}
