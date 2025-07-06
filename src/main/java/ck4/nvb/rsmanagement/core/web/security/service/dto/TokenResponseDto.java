package ck4.nvb.rsmanagement.core.web.security.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import lombok.*;

import java.io.Serial;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TokenResponseDto extends Dto {


    @Serial
    private static final long serialVersionUID = 1L;

    private String accessToken;

    private String tokenType = "Bearer";

    private String refreshToken;

    private long issuedAt;

    private long expiresIn;

    private long expiresAt;

    @Override
    public String toString() {
        return "TokenResponseDto{" +
                "accessToken='" + accessToken + '\'' +
                ", type='" + tokenType + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", issuedAt=" + issuedAt +
                ", expiresIn=" + expiresIn +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
