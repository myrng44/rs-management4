package develop.circlek.service.interfacee.user;

import develop.circlek.dto.user.User_roleCreationRequest;
import develop.circlek.dto.user.User_roleUpdateRequest;
import develop.circlek.entity.user.User_role;

import java.util.List;

public interface User_roleService {
    User_role createUser_role(User_roleCreationRequest request);
    User_role updateUser_role(String user_roleId, User_roleUpdateRequest request);
    List<User_role> getUser_roles();
    User_role getUser_role(String user_roleId);
    void deleteUser_role();
}
