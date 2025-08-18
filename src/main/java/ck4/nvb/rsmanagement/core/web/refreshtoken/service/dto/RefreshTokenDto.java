package ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.base.util.InputUtils;
import ck4.nvb.rsmanagement.core.web.refreshtoken.domain.RefreshToken;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class RefreshTokenDto extends EntityDto<String>
    implements CreateInput<RefreshToken>, UpdateInput<RefreshToken> {

  private String ipAddress;

  private String deviceSession;

  private LocalDateTime expiredTime;

  public void setIpAddress(String ipAddress) {
    this.ipAddress = InputUtils.getString(ipAddress);
  }

  public void setDeviceSession(String deviceSession) {
    this.deviceSession = InputUtils.getString(deviceSession);
  }

  @Override
  public RefreshToken mapToEntity() {
    return new ModelMapper().map(this, RefreshToken.class);
  }

  @Override
  public boolean mapToEntity(RefreshToken entity) {
    boolean result = false;

    if (ipAddress != null) {
      entity.setIpAddress(InputUtils.getString(ipAddress));
      result = true;
    }

    if (deviceSession != null) {
      entity.setDeviceSession(InputUtils.getString(deviceSession));
      result = true;
    }

    if (expiredTime != null) {
      entity.setExpiredTime(expiredTime);
      result = true;
    }

    return result;
  }
}
