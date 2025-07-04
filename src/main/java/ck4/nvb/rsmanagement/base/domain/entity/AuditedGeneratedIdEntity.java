package ck4.nvb.rsmanagement.base.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.interfaces.Audited;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Generated;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AuditedGeneratedIdEntity extends CreationAuditedGeneratedIdEntity implements Audited<Long> {
    // Fields
    @Column(name = "last_updated")
    private LocalDateTime lastUpdatedTime;

    @Column(name = "last_updater")
    private Long updaterId;

    @Generated
    public LocalDateTime getUpdatedTime() {
        return lastUpdatedTime;
    }

    @Generated
    public void setUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Generated
    public Long getUpdaterID() {
        return updaterId;
    }

    @Generated
    public void setUpdaterID(Long updaterId) {
        this.updaterId = updaterId;
    }
}
