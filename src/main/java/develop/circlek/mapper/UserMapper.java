package develop.circlek.mapper;

import develop.circlek.dto.user.UserCreationRequest;
import develop.circlek.dto.user.UserUpdateRequest;
import develop.circlek.dto.user.User_roleCreationRequest;
import develop.circlek.entity.user.User;
import develop.circlek.entity.user.User_role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
