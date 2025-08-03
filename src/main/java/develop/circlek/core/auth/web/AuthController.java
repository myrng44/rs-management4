package develop.circlek.core.auth.web;

import develop.circlek.base.application.exception.NotFoundException;
import develop.circlek.core.auth.application.service.AuthService;
import develop.circlek.core.user.application.dto.UserDTO;
import develop.circlek.core.user.application.dto.request.LoginRequest;
import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.core.user.application.dto.response.LoginResponse;
import develop.circlek.core.user.application.service.UserService;
import develop.circlek.core.user.domain.entity.UserEntity;
import develop.circlek.core.user.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("Login failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token == null) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error(401, "No token provided"));
            }

            Claims claims = getClaims(token);
            String username = claims.getSubject();

            UserEntity user = userRepository.findByUserNameWithRoles(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            // Get roles and permissions
            List<String> roles = authService.getUserRoles(user.getId());
            List<String> permissions = authService.getUserPermissions(user.getId());

            UserDTO userDTO = UserDTO.builder()
                    .userName(user.getUserName())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .storeId(user.getStoreId())
                    .lastLogin(user.getLastLogin())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
            userDTO.setId(user.getId());
            userDTO.setCreateAt(user.getCreateAt());
            userDTO.setCreateBy(user.getCreateBy());
            userDTO.setUpdateAt(user.getUpdateAt());
            userDTO.setUpdateBy(user.getUpdateBy());

            return ResponseEntity.ok(ApiResponse.success(userDTO));
        } catch (Exception e) {
            log.error("Get current user failed", e);
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(401, "Invalid token"));
        }
    }

    // Helper methods - thêm vào AuthController
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Claims getClaims(String token) {
        Key key = new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
                "HmacSHA256");
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }
}