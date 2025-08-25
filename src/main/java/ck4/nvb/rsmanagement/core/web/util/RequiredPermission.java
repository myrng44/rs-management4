package ck4.nvb.rsmanagement.core.web.util;

import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    PermissionCode value();
    boolean requireStoreAccess() default true;
}
