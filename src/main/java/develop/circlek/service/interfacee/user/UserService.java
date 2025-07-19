package develop.circlek.service.interfacee.user;

import develop.circlek.dto.request.CreateUserRequest;
import develop.circlek.dto.request.LoginRequest;
import develop.circlek.dto.request.UpdateUserRequest;
import develop.circlek.dto.response.LoginResponse;
import develop.circlek.dto.response.UserDTO;
import develop.circlek.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest request);
    UserDTO createUser(CreateUserRequest request);
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long userId, UpdateUserRequest request);
    void deleteUser(Long userId);
}