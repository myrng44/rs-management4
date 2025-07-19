package develop.circlek.aspect;

import develop.circlek.annotation.RequirePermission;
import develop.circlek.service.interfacee.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionAspect {

    private final AuthService authService;

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        Long currentUserId = getCurrentUserId();

        log.debug("Current userId from SecurityContext: {}", currentUserId);

        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        String requiredPermission = requirePermission.value();
        log.debug("Required permission: {}", requiredPermission);

        if (!authService.hasPermission(currentUserId, requiredPermission)) {
            throw new RuntimeException("Access denied. Required permission: " + requiredPermission);
        }

        return joinPoint.proceed();
    }

    private Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            return authService.getUserIdByUsername(username);
        }
        return null;
    }
}