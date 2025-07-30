package ck4.nvb.rsmanagement.core.module.stores.importlog.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.stores.importlog.domain.ImportLog;
import ck4.nvb.rsmanagement.core.module.stores.importlog.service.ImportLogServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.importlog.service.dto.ImportLogDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/import-log")
public class ImportLogController extends AuditedCrudController<ImportLogDto, ImportLog, String, UserGetDto, Long, ImportLogDto, ImportLogDto> {

    public ImportLogController(ImportLogServiceImpl importLogService) {
        super(importLogService);
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
