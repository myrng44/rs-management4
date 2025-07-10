package develop.circlek.service.interfacee.user;

import develop.circlek.dto.user.UserCreationRequest;
import develop.circlek.dto.user.UserUpdateRequest;
import develop.circlek.entity.user.User;

import java.util.List;

public interface UserService {
    User createUser(UserCreationRequest request);
    List<User> getUsers();
    User getUser(String userId);
    User updateUser(String userId, UserUpdateRequest request);
    void deleteUser(String userId);
}
