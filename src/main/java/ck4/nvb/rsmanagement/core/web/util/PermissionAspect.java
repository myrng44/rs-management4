package ck4.nvb.rsmanagement.core.web.util;

import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.web.security.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionAspect {
    private final AuthorizationService authorizationService;

    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequiredPermission requirePermission) {
        try {
            PermissionCode permission = requirePermission.value();
            Long storeId = extractStoreId(joinPoint, requirePermission.requireStoreAccess());

            if (storeId != null) {
                authorizationService.requirePermissionAtStore(permission, storeId);
            } else {
                authorizationService.requirePermission(permission);
            }

            log.debug("Permission check passed: {} for method: {}",
                    permission, joinPoint.getSignature().getName());

        } catch (Exception e) {
            log.warn("Permission check failed for method: {}",
                    joinPoint.getSignature().getName(), e);
            throw e;
        }
    }

    @Before("@annotation(requireAnyPermission)")
    public void checkAnyPermission(JoinPoint joinPoint, RequiredAnyPermission requireAnyPermission) {
        PermissionCode[] permissions = requireAnyPermission.value();
        Long storeId = extractStoreId(joinPoint, requireAnyPermission.requireStoreAccess());

        for (PermissionCode permission : permissions) {
            try {
                if (storeId != null) {
                    if (authorizationService.hasPermissionAtStore(permission, storeId)) {
                        return;
                    }
                } else {
                    if (authorizationService.hasPermission(permission)) {
                        return;
                    }
                }
            } catch (Exception e) {
                log.debug("Permission check failed: {}", permission, e);
            }
        }

        throw new SecurityException("Access denied: none of the required permissions found");
    }

    /**
     * Extract storeId from method parameters
     */
    private Long extractStoreId(JoinPoint joinPoint, boolean requireStoreAccess) {
        if (!requireStoreAccess) {
            return null;
        }

        // Try to find storeId in method parameters
        Object[] args = joinPoint.getArgs();
        String[] paramNames = getParameterNames(joinPoint);

        for (int i = 0; i < args.length && i < paramNames.length; i++) {
            if ("storeId".equals(paramNames[i]) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }

        // If no storeId parameter found, use current user's store
        return authorizationService.getCurrentStoreId();
    }

    private String[] getParameterNames(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getParameterNames();
    }
}
