package ck4.nvb.rsmanagement.core.module.stores.importedproduct.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.service.IBatchService;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/batch")
public class BatchController extends AuditedCrudController<BatchDto, Batch, Long, UserGetDto, Long, BatchDto, BatchDto> {

    protected BatchController(IBatchService batchCrudService) {
        super(batchCrudService);
    }

    @Override
    public UserGetDto extractUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserGetDto) {
            return (UserGetDto) principal;
        } else if (principal instanceof UserRoleDto) {
            return new ModelMapper().map(principal, UserGetDto.class);
        }

        return null;
    }
}
