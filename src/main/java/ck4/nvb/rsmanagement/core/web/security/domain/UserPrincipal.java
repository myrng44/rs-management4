package ck4.nvb.rsmanagement.core.web.security.domain;

import java.io.Serializable;
import java.security.Principal;

public class UserPrincipal implements Principal, Serializable {
  private String username;

  @Override
  public String getName() {
    return username;
  }
}
