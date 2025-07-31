package develop.circlek.core.user.web;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.user.domain.entity.UserEntity;
import develop.circlek.core.user.application.dto.UserDTO;
import develop.circlek.core.user.application.service.UserService;
import develop.circlek.core.user.application.dto.request.CreateUserRequest;
import develop.circlek.base.application.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController extends BaseController<UserEntity, UserDTO, Long> {

    private final UserService userService;

    @Override
    protected BaseService<UserEntity, UserDTO, Long> getService() {
        return userService;
    }

    @PostMapping("/create-with-roles")
    public ResponseEntity<ApiResponse<UserDTO>> createUserWithRoles(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO user = userService.createUser(request);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            log.error("Create user with roles failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}