package develop.circlek.core.user.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.user.domain.entity.UserEntity;
import develop.circlek.core.user.domain.entity.RoleEntity;
import develop.circlek.core.user.domain.entity.UserRoleEntity;
import develop.circlek.core.user.domain.repository.UserRepository;
import develop.circlek.core.user.domain.repository.UserRoleRepository;
import develop.circlek.core.user.domain.repository.RoleRepository;
import develop.circlek.core.user.domain.repository.RolePermissionRepository;
import develop.circlek.core.user.application.dto.UserDTO;
import develop.circlek.core.user.application.dto.request.CreateUserRequest;
import develop.circlek.core.user.application.dto.request.LoginRequest;
import develop.circlek.core.user.application.dto.request.UpdateUserRequest;
import develop.circlek.core.user.application.dto.response.LoginResponse;
import develop.circlek.core.auth.application.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends BaseService<UserEntity, UserDTO, Long> {

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

    @Override
    protected BaseRepository<UserEntity, Long> getRepository() {
        return userRepository;
    }

    @Override
    protected UserDTO convertToDTO(UserEntity entity) {
        List<String> roles = authService.getUserRoles(entity.getId());
        List<String> permissions = rolePermissionRepository.findPermissionCodesByRoleIds(
                userRoleRepository.findByUserId(entity.getId()).stream()
                        .map(ur -> ur.getRole().getId())
                        .collect(Collectors.toSet())
        );

        return UserDTO.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .storeId(entity.getStoreId())
                .lastLogin(entity.getLastLogin())
                .createAt(entity.getCreateAt())
                .createBy(entity.getCreateBy())
                .updateAt(entity.getUpdateAt())
                .updateBy(entity.getUpdateBy())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Override
    protected UserEntity convertToEntity(UserDTO dto) {
        return UserEntity.builder()
                .userName(dto.getUserName())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .storeId(dto.getStoreId())
                .lastLogin(dto.getLastLogin())
                .build();
    }

    @Override
    protected void updateEntityFromDTO(UserEntity entity, UserDTO dto) {
        if (dto.getFullName() != null) entity.setFullName(dto.getFullName());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getPhone() != null) entity.setPhone(dto.getPhone());
        if (dto.getStoreId() != null) entity.setStoreId(dto.getStoreId());

        Long currentUserId = getCurrentUserId();
        entity.setUpdateAt(LocalDateTime.now());
        entity.setUpdateBy(currentUserId);
    }

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
    public UserDTO createUser(CreateUserRequest request) {
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

        return UserDTO.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .storeId(savedUser.getStoreId())
                .lastLogin(savedUser.getLastLogin())
                .createAt(savedUser.getCreateAt())
                .createBy(savedUser.getCreateBy())
                .updateAt(savedUser.getUpdateAt())
                .updateBy(savedUser.getUpdateBy())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Transactional
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

        return UserDTO.builder()
                .id(updatedUser.getId())
                .userName(updatedUser.getUserName())
                .fullName(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .phone(updatedUser.getPhone())
                .storeId(updatedUser.getStoreId())
                .lastLogin(updatedUser.getLastLogin())
                .createAt(updatedUser.getCreateAt())
                .createBy(updatedUser.getCreateBy())
                .updateAt(updatedUser.getUpdateAt())
                .updateBy(updatedUser.getUpdateBy())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Override
    public UserDTO findById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new RuntimeException("User not found");
                });

        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Fetching all users");
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new RuntimeException("User not found: " + userId);
                });
        userRoleRepository.deleteByUserId(userId);
        userRepository.delete(user);
        log.info("Deleted user with id: {}", userId);
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