package ck4.nvb.rsmanagement.core.web.refreshtoken.service;

import ck4.nvb.rsmanagement.base.application.service.CreationAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.web.refreshtoken.domain.RefreshToken;
import ck4.nvb.rsmanagement.core.web.refreshtoken.domain.RefreshTokenRepository;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto.RefreshTokenDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("refreshTokenCrudService")
public class RefreshTokenCrudServiceImpl extends CreationAuditedCrudServiceImpl<RefreshTokenDto, RefreshToken, String, UserGetDto, Long> implements RefreshTokenCrudService {

    private final ModelMapper modelMapper = new ModelMapper();

    protected RefreshTokenCrudServiceImpl(RefreshTokenRepository repository) {
        super(repository, RefreshToken.class);
    }

    @Override
    public RefreshTokenRepository getRepository() {
        return (RefreshTokenRepository) super.getRepository();
    }

    @Override
    public RefreshTokenDto mapToEntityDto(RefreshToken entity) {
        return modelMapper.map(entity, RefreshTokenDto.class);
    }

    @Override
    public void deleteAll(String deviceSession, Long creatorId) {
        getRepository().deleteByCreatorIdAndDeviceSession(creatorId, deviceSession);
    }
}
