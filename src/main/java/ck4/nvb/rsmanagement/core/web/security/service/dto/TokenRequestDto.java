package ck4.nvb.rsmanagement.core.web.security.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto extends CredentialInput {

  // pptional: specify store to log in to
  private Long storeId;

  private String ipAddress;

  private String deviceSession;

  private String traceId;

  public TokenRequestDto(CredentialInput credential) {
    setPassword(credential.getPassword());
    setUsername(credential.getUsername());
  }
}
