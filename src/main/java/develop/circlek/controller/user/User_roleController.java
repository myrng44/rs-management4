package develop.circlek.controller.user;

import develop.circlek.dto.user.ApiResponse;
import develop.circlek.dto.user.User_roleCreationRequest;
import develop.circlek.dto.user.User_roleUpdateRequest;
import develop.circlek.entity.user.User_role;
import develop.circlek.service.interfacee.user.User_roleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User_roleController {
    @Autowired
    User_roleService user_roleService;

    @PostMapping
    ApiResponse<User_role> createUser_role(@RequestBody @Valid User_roleCreationRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(user_roleService.createUser_role(request));
        return apiResponse;
    }

    @GetMapping
    List<User_role> getUser_roles() {
        return user_roleService.getUser_roles();
    }

    @GetMapping("/{user_roleId}")
    User_role getUser_role(@PathVariable("/user_roleId") String user_roleId) {
        return user_roleService.getUser_role(user_roleId);
    }

    @PutMapping("/{user_roleId}")
    User_role updateUser_role(@PathVariable("/user_roleId") String user_roleId, @RequestBody User_roleUpdateRequest request) {
        return user_roleService.updateUser_role(user_roleId, request);
    }

    @DeleteMapping
    String deleteUser_role() {
        user_roleService.deleteUser_role();
        return "user_role has been deleted";
    }
}
