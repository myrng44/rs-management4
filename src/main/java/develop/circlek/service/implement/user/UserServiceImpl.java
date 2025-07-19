package develop.circlek.service.implement.user;

import develop.circlek.dto.request.CreateUserRequest;
import develop.circlek.dto.request.LoginRequest;
import develop.circlek.dto.request.UpdateUserRequest;
import develop.circlek.dto.response.LoginResponse;
import develop.circlek.dto.response.UserDTO;
import develop.circlek.entity.user.RoleEntity;
import develop.circlek.entity.user.UserEntity;
import develop.circlek.entity.user.UserRoleEntity;
import develop.circlek.repository.RoleRepository;
import develop.circlek.repository.UserRepository;
import develop.circlek.repository.UserRoleRepository;
import develop.circlek.repository.RolePermissionRepository;
import develop.circlek.service.interfacee.auth.AuthService;
import develop.circlek.service.interfacee.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    public LoginResponse login(LoginRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByUserNameWithRoles(request.getUserName());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassWord(), user.getPassWord())) {
            throw new RuntimeException("Invalid password");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        List<String> roles = authService.getUserRoles(user.getId());
        List<String> permissions = authService.getUserPermissions(user.getId());

        String token = generateJwtToken(user.getId(), user.getUserName(), roles, permissions);

        return LoginResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .storeId(user.getStoreId())
                .lastLogin(user.getLastLogin())
                .token(token)
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Transactional
    @Override
    public UserDTO createUser(CreateUserRequest request) {
        log.info("Creating user with username: {}", request.getUserName());

        if (userRepository.existsByUserName(request.getUserName())) {
            log.error("Username already exists: {}", request.getUserName());
            throw new RuntimeException("Username already exists");
        }

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        Long currentUserId = getCurrentUserId();

        UserEntity user = UserEntity.builder()
                .userName(request.getUserName())
                .passWord(passwordEncoder.encode(request.getPassWord()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .storeId(request.getStoreId())
                .createAt(LocalDateTime.now())
                .createBy(currentUserId)
                .build();

        UserEntity savedUser = userRepository.save(user);
        log.info("Saved user: id={}, username={}", savedUser.getId(), savedUser.getUserName());

        Set<Long> roleIds = request.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRoleEntity> userRoles = roleIds.stream()
                    .map(roleId -> {
                        if (!roleRepository.existsById(roleId)) {
                            log.warn("Role ID not found: {}", roleId);
                            return null;
                        }
                        RoleEntity role = roleRepository.findById(roleId)
                                .orElseThrow(() -> {
                                    log.error("Role not found: {}", roleId);
                                    return new RuntimeException("Role not found: " + roleId);
                                });
                        return UserRoleEntity.builder()
                                .userId(savedUser.getId())
                                .roleId(roleId)
                                .user(savedUser)
                                .role(role)
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!userRoles.isEmpty()) {
                userRoleRepository.saveAll(userRoles);
                log.info("Assigned {} roles to user: {}", userRoles.size(), savedUser.getUserName());
            } else {
                log.warn("No valid role IDs provided for user: {}", savedUser.getUserName());
            }
        } else {
            log.warn("No role IDs provided for user: {}", savedUser.getUserName());
        }

        List<String> roles = authService.getUserRoles(savedUser.getId());
        List<String> permissions = rolePermissionRepository.findPermissionCodesByRoleIds(roleIds);

        log.info("User created with roles: {}, permissions: {}", roles, permissions);

        return new UserDTO(
                savedUser.getId(),
                savedUser.getUserName(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getStoreId(),
                savedUser.getLastLogin(),
                savedUser.getCreateAt(),
                savedUser.getCreateBy(),
                savedUser.getUpdateAt(),
                savedUser.getUpdateBy(),
                roles,
                permissions
        );
    }

    @Transactional
    @Override
    public UserDTO updateUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user with id: {}", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new RuntimeException("User not found: " + userId);
                });

        Long currentUserId = getCurrentUserId();

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getStoreId() != null) user.setStoreId(request.getStoreId());
        if (request.getPassWord() != null) {
            user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        }

        user.setUpdateAt(LocalDateTime.now());
        user.setUpdateBy(currentUserId);

        UserEntity updatedUser = userRepository.save(user);
        log.info("Updated user: id={}, username={}", updatedUser.getId(), updatedUser.getUserName());

        List<String> roles = authService.getUserRoles(updatedUser.getId());
        List<String> permissions = rolePermissionRepository.findPermissionCodesByRoleIds(
                userRoleRepository.findByUserId(userId).stream()
                        .map(ur -> ur.getRole().getId())
                        .collect(Collectors.toSet())
        );

        log.info("User updated with roles: {}, permissions: {}", roles, permissions);

        return new UserDTO(
                updatedUser.getId(),
                updatedUser.getUserName(),
                updatedUser.getFullName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getStoreId(),
                updatedUser.getLastLogin(),
                updatedUser.getCreateAt(),
                updatedUser.getCreateBy(),
                updatedUser.getUpdateAt(),
                updatedUser.getUpdateBy(),
                roles,
                permissions
        );
    }

    @Override
    public UserDTO getUserById(Long userId) {
        log.info("Fetching user with id: {}", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new RuntimeException("User not found");
                });

        List<String> roles = authService.getUserRoles(userId);
        List<String> permissions = rolePermissionRepository.findPermissionCodesByRoleIds(
                userRoleRepository.findByUserId(userId).stream()
                        .map(ur -> ur.getRole().getId())
                        .collect(Collectors.toSet())
        );

        log.info("Fetched user with roles: {}, permissions: {}", roles, permissions);

        return new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getStoreId(),
                user.getLastLogin(),
                user.getCreateAt(),
                user.getCreateBy(),
                user.getUpdateAt(),
                user.getUpdateBy(),
                roles,
                permissions
        );
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");

        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    List<String> roles = authService.getUserRoles(user.getId());
                    List<String> permissions = rolePermissionRepository.findPermissionCodesByRoleIds(
                            userRoleRepository.findByUserId(user.getId()).stream()
                                    .map(ur -> ur.getRole().getId())
                                    .collect(Collectors.toSet())
                    );
                    return new UserDTO(
                            user.getId(),
                            user.getUserName(),
                            user.getFullName(),
                            user.getEmail(),
                            user.getPhone(),
                            user.getStoreId(),
                            user.getLastLogin(),
                            user.getCreateAt(),
                            user.getCreateBy(),
                            user.getUpdateAt(),
                            user.getUpdateBy(),
                            roles,
                            permissions
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new RuntimeException("User not found: " + userId);
                });
        userRoleRepository.deleteByUserId(userId);
        userRepository.delete(user);
        log.info("Deleted user with id: {}", userId);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            return authService.getUserIdByUsername(username);
        }
        log.warn("No authenticated user found");
        return null;
    }

    private String generateJwtToken(Long userId, String username, List<String> roles, List<String> permissions) {
        if (roles == null) roles = new ArrayList<>();
        if (permissions == null) permissions = new ArrayList<>();
        Key key = new SecretKeySpec(Base64.getDecoder().decode(JWT_SECRET), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}