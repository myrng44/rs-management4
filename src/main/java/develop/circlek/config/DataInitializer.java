package develop.circlek.config;

import develop.circlek.entity.user.*;
import develop.circlek.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing sample data...");

            PermissionEntity userRead = createPermission("USER_READ", "Read user information");
            PermissionEntity userWrite = createPermission("USER_WRITE", "Create and update users");
            PermissionEntity userDelete = createPermission("USER_DELETE", "Delete users");
            PermissionEntity roleRead = createPermission("ROLE_READ", "Read roles");
            PermissionEntity roleWrite = createPermission("ROLE_WRITE", "Create and update roles");
            PermissionEntity adminAccess = createPermission("ADMIN_ACCESS", "Full admin access");

            RoleEntity adminRole = createRole("ADMIN", "System Administrator");
            RoleEntity managerRole = createRole("MANAGER", "Store Manager");
            RoleEntity employeeRole = createRole("EMPLOYEE", "Store Employee");

            assignPermissionsToRole(adminRole, Arrays.asList(userRead, userWrite, userDelete, roleRead, roleWrite, adminAccess));
            assignPermissionsToRole(managerRole, Arrays.asList(userRead, userWrite, roleRead, roleWrite));
            assignPermissionsToRole(employeeRole, Arrays.asList(userRead, roleRead));

            UserEntity admin = createUser("admin", "password123", "System Admin", "admin@circlek.com", "0123456789", 1L);
            UserEntity manager = createUser("manager1", "password123", "Store Manager", "manager1@circlek.com", "0123456788", 1L);
            UserEntity employee = createUser("employee1", "password123", "Store Employee", "employee1@circlek.com", "0123456787", 1L);

            userRepository.save(admin);
            userRepository.save(manager);
            userRepository.save(employee);

            assignRoleToUser(admin, adminRole);
            assignRoleToUser(manager, managerRole);
            assignRoleToUser(employee, employeeRole);

            log.info("Sample data initialized successfully!");
        }
    }

    private PermissionEntity createPermission(String code, String description) {
        PermissionEntity permission = PermissionEntity.builder()
                .code(code)
                .permissionDesc(description)
                .build();
        return permissionRepository.save(permission);
    }

    private RoleEntity createRole(String name, String description) {
        RoleEntity role = RoleEntity.builder()
                .name(name)
                .description(description)
                .createAt(LocalDateTime.now())
                .createBy(1L)
                .deleted(false)
                .build();
        return roleRepository.save(role);
    }

    private UserEntity createUser(String userName, String password, String fullName, String email, String phone, Long storeId) {
        UserEntity user = UserEntity.builder()
                .userName(userName)
                .passWord(passwordEncoder.encode(password))
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .storeId(storeId)
                .createAt(LocalDateTime.now())
                .createBy(1L)
                .updateBy(null)
                .build();
        return userRepository.save(user);
    }

    private void assignPermissionsToRole(RoleEntity role, java.util.List<PermissionEntity> permissions) {
        permissions.forEach(permission -> {
            RolePermissionEntity rolePermission = RolePermissionEntity.builder()
                    .roleId(role.getId())
                    .permissionId(permission.getId())
                    .build();
            rolePermissionRepository.save(rolePermission);
        });
    }

    private void assignRoleToUser(UserEntity user, RoleEntity role) {
        UserRoleEntity userRole = UserRoleEntity.builder()
                .userId(user.getId())
                .roleId(role.getId())
                .user(user)
                .role(role)
                .build();
        userRoleRepository.save(userRole);
        log.info("Assigned role {} to user {} with userId: {}, roleId: {}",
                role.getName(), user.getUserName(), user.getId(), role.getId());
    }
}