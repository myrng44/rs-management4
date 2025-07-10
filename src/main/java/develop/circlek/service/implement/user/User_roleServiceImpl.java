package develop.circlek.service.implement.user;

import develop.circlek.dto.user.User_roleCreationRequest;
import develop.circlek.dto.user.User_roleUpdateRequest;
import develop.circlek.entity.user.User_role;
import develop.circlek.mapper.User_roleMapper;
import develop.circlek.repository.user.User_roleRepository;
import develop.circlek.service.interfacee.user.User_roleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)

public class User_roleServiceImpl implements User_roleService {
    @Autowired
    User_roleRepository user_roleRepository;

    @Autowired
    User_roleMapper user_roleMapper;

    @Override
    public User_role createUser_role(User_roleCreationRequest  request) {
        User_role user_role = user_roleMapper.toUser_role(request);
        return user_roleRepository.save(user_role);
    }

    @Override
    public User_role updateUser_role(String user_roleId, User_roleUpdateRequest request) {
        User_role user_role = user_roleRepository.findById(user_roleId).orElseThrow(() -> new RuntimeException("user_role not found"));
        user_roleMapper.updateUser_role(user_role, request);
        return user_roleRepository.save(user_role);
    }

    @Override
    public User_role getUser_role(String user_roleId) {
        return user_roleRepository.findById(user_roleId).orElseThrow(() -> new RuntimeException("user_role not found"));
    }

    @Override
    public List<User_role> getUser_roles() {
        return user_roleRepository.findAll();
    }

    @Override
    public void deleteUser_role() {
        user_roleRepository.deleteAll();
    }
}
