package ck4.nvb.rsmanagement.core.web.refreshtoken.domain;

import ck4.nvb.rsmanagement.base.domain.entity.CreationAuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter @Setter
public class RefreshToken extends CreationAuditedEntity<String, Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device_session")
    private String deviceSession;

    @Column(name = "expired_time", nullable = false)
    private LocalDateTime expiredTime;

    public RefreshToken() {
        super();
    }

    public RefreshToken(String id) {
        setId(id);
    }
}
