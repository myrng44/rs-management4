package ck4.nvb.rsmanagement.core.module.users.user.service.impl;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.users.user.domain.User;
import ck4.nvb.rsmanagement.core.module.users.user.domain.UserRepository;
import ck4.nvb.rsmanagement.core.module.users.user.service.UserCrudService;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.BaseUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("userCrudService")
public class UserCrudServiceImpl extends FullAuditedCrudServiceImpl<BaseUserDto, User, Long, BaseUserDto, Long> implements UserCrudService {

    private final ModelMapper modelMapper = new ModelMapper();

    protected UserCrudServiceImpl(UserRepository repository) {
        super(repository, User.class);
    }

    @Override
    public UserRepository getRepository() {
        return (UserRepository) super.getRepository();
    }

    @Override
    public BaseUserDto mapToEntityDto(User entity) {
        return modelMapper.map(entity, BaseUserDto.class);
    }
}
