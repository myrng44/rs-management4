package ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenGetDto extends EntityDto<String> {

  private String ipAddress;
  private String deviceSession;
  private LocalDateTime expiredTime;
  private Long creatorId;
}
