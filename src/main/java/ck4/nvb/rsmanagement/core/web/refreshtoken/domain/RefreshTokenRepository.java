package ck4.nvb.rsmanagement.core.web.refreshtoken.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("refreshTokenRepository")
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, String> {

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken u WHERE u.creatorId = ?1 AND u.deviceSession = ?2")
    void deleteByCreatorIdAndDeviceSession(Long userId, String deviceSession);
}
