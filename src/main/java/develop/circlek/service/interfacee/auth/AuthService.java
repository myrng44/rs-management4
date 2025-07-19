package develop.circlek.service.interfacee.auth;

import develop.circlek.dto.request.LoginRequest;

import java.util.List;

public interface AuthService {
    List<String> getUserRoles(Long userId);
    List<String> getUserPermissions(Long userId);
    boolean hasPermission(Long userId, String permissionCode);
    boolean hasRole(Long userId, String roleName);
    Long getUserIdByUsername(String username);
}