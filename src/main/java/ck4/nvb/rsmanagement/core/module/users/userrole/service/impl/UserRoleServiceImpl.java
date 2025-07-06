package ck4.nvb.rsmanagement.core.module.users.userrole.service.impl;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.entity.UserRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.repository.UserRoleRepository;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.UserRoleService;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userRoleService")
public class UserRoleServiceImpl extends FullAuditedCrudServiceImpl<UserRoleDto, UserRole, Long, UserGetDto, Long> implements UserRoleService {

    private final ModelMapper modelMapper = new ModelMapper();

    protected UserRoleServiceImpl(UserRoleRepository repository) {
        super(repository, UserRole.class);
    }

    @Override
    public UserRoleRepository getRepository() {
        return (UserRoleRepository) super.getRepository();
    }

    @Override
    public UserRoleDto mapToEntityDto(UserRole entity) {
        return modelMapper.map(entity, UserRoleDto.class);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("user_id", List.of(SearchOperator.EQUALS));
        keys.put("store_id", List.of(SearchOperator.EQUALS));
        return keys;
    }
}
