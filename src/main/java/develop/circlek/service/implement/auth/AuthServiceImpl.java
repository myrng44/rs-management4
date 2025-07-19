package develop.circlek.service.implement.auth;

import develop.circlek.entity.user.UserEntity;
import develop.circlek.entity.user.UserRoleEntity;
import develop.circlek.repository.UserRepository;
import develop.circlek.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements develop.circlek.service.interfacee.auth.AuthService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;


    public List<String> getUserRoles(Long userId) {
        log.debug("Fetching roles for userId: {}", userId);
        List<UserRoleEntity> userRoles = userRoleRepository.findByUserIdWithPermissions(userId);
        log.debug("User roles found: {}", userRoles.size());

        return userRoles.stream()
                .map(ur -> {
                    if (ur.getRole() == null) {
                        log.warn("Role is null for UserRoleEntity with userId: {}, roleId: {}", userId, ur.getRoleId());
                        return null;
                    }
                    log.debug("Found role: {} for user: {}", ur.getRole().getName(), userId);
                    return ur.getRole().getName();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        log.debug("Fetching permissions for userId: {}", userId);
        List<UserRoleEntity> userRoles = userRoleRepository.findByUserIdWithPermissions(userId);
        log.debug("User roles found for permissions: {}", userRoles.size());

        return userRoles.stream()
                .filter(ur -> ur.getRole() != null)
                .flatMap(ur -> {
                    if (ur.getRole().getRolePermissions() == null) {
                        log.warn("Role permissions is null for role: {}", ur.getRole().getName());
                        return Stream.empty();
                    }
                    return ur.getRole().getRolePermissions().stream();
                })
                .filter(rp -> rp.getPermission() != null)
                .map(rp -> rp.getPermission().getCode())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<String> permissions = getUserPermissions(userId);
        return permissions.contains(permissionCode);
    }

    @Override
    public boolean hasRole(Long userId, String roleName) {
        List<String> roles = getUserRoles(userId);
        return roles.contains(roleName);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(UserEntity::getId)
                .orElse(null);
    }
}