package ck4.nvb.rsmanagement.core.module.stores.store.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.stores.store.domain.Store;
import ck4.nvb.rsmanagement.core.module.stores.store.service.StoreCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.store.service.dto.StoreDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/store")
public class StoreController extends AuditedCrudController<StoreDto, Store, Long, UserGetDto, Long, StoreDto, StoreDto> {

    public StoreController(StoreCrudServiceImpl storeCrudService) {
        super(storeCrudService);
    }

    @Override
    public UserGetDto extractUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserGetDto) {
            return (UserGetDto) principal;
        }

        if (principal instanceof UserRoleDto) {
            return new ModelMapper().map(principal, UserGetDto.class);
        }

        return null;
    }
}
