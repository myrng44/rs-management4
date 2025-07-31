package develop.circlek.core.user.web;

import develop.circlek.base.application.annotation.RequirePermission;
import develop.circlek.core.user.application.dto.request.CreateUserRequest;
import develop.circlek.core.user.application.dto.request.UpdateUserRequest;
import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.core.user.application.dto.UserDTO;
import develop.circlek.core.user.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/with-permissions")
@RequiredArgsConstructor
@Slf4j
public class UserControllerWithPermissions {

    private final UserService userService;

    @PostMapping
    @RequirePermission("USER_WRITE")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            UserDTO user = userService.createUser(request);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            log.error("Create user failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @RequirePermission("USER_READ")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.findById(id);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            log.error("Get user failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping
    @RequirePermission("USER_READ")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.findAll();
            return ResponseEntity.ok(ApiResponse.success(users));
        } catch (Exception e) {
            log.error("Get all users failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }

    @PutMapping("/{userId}")
    @RequirePermission("USER_WRITE")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserWithPermissions(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        try {
            // Convert UpdateUserRequest to UserDTO for base service
            UserDTO userDTO = UserDTO.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .storeId(request.getStoreId())
                    .build();
            
            UserDTO updatedUser = userService.update(userId, userDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedUser));
        } catch (Exception e) {
            log.error("Update user failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @RequirePermission("USER_DELETE")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            log.info("Deleting user with id: {}", id);
            userService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (RuntimeException e) {
            log.error("Delete user failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}