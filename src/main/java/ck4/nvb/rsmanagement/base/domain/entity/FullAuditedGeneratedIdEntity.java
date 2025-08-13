package ck4.nvb.rsmanagement.base.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.interfaces.FullAudited;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Generated;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class FullAuditedGeneratedIdEntity extends AuditedGeneratedIdEntity implements FullAudited<Long> {
    // Fields
    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @Column(name = "deleter_id")
    private Long deleterId;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Generated
    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    @Generated
    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }

    @Generated
    public Long getDeleterID() {
        return deleterId;
    }

    @Generated
    public void setDeleterID(Long deleterID) {
        this.deleterId = deleterID;
    }

    @Generated
    public boolean isDeleted() {
        return deleted;
    }

    @Generated
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
