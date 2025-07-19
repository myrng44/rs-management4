package develop.circlek.controller.user;

import develop.circlek.dto.response.ApiResponse;
import develop.circlek.entity.user.RoleEntity;
import develop.circlek.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleEntity>>> getAllRoles() {
        try {
            List<RoleEntity> roles = roleRepository.findByDeletedFalse();
            return ResponseEntity.ok(ApiResponse.success(roles));
        } catch (Exception e) {
            log.error("Get all roles failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleEntity>> getRoleById(@PathVariable Long id) {
        try {
            RoleEntity role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            return ResponseEntity.ok(ApiResponse.success(role));
        } catch (Exception e) {
            log.error("Get role failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}