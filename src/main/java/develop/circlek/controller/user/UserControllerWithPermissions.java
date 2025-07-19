package develop.circlek.controller.user;

import develop.circlek.annotation.RequirePermission;
import develop.circlek.dto.request.CreateUserRequest;
import develop.circlek.dto.request.UpdateUserRequest;
import develop.circlek.dto.response.ApiResponse;
import develop.circlek.dto.response.UserDTO;
import develop.circlek.service.interfacee.user.UserService;
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
            UserDTO user = userService.getUserById(id);
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
            List<UserDTO> users = userService.getAllUsers();
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
            UserDTO userDTO = userService.updateUser(userId, request);
            return ResponseEntity.ok(ApiResponse.success(userDTO));
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
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (RuntimeException e) {
            log.error("Delete user failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}