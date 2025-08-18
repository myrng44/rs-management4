package ck4.nvb.rsmanagement.core.web.security.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialInput extends Dto {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @NotNull(message = "Password is required")
  @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
  private String password;
}
