package ck4.nvb.rsmanagement.core.web.refreshtoken.service.impl;

import ck4.nvb.rsmanagement.base.application.service.GetServiceImpl;
import ck4.nvb.rsmanagement.core.web.refreshtoken.domain.RefreshToken;
import ck4.nvb.rsmanagement.core.web.refreshtoken.domain.RefreshTokenRepository;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.RefreshTokenGetService;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto.RefreshTokenGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("refreshTokenGetService")
public class RefreshTokenGetServiceImpl extends GetServiceImpl<RefreshTokenGetDto, RefreshToken, String> implements RefreshTokenGetService {

    private final ModelMapper modelMapper = new ModelMapper();

    protected RefreshTokenGetServiceImpl(RefreshTokenRepository repository) {
        super(repository, RefreshToken.class);
    }

    @Override
    public RefreshTokenRepository getRepository() {
        return (RefreshTokenRepository) super.getRepository();
    }

    @Override
    public RefreshTokenGetDto mapToEntityDto(RefreshToken entity) {
        return modelMapper.map(entity, RefreshTokenGetDto.class);
    }
}