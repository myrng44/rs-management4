package develop.circlek.mapper;

import develop.circlek.dto.UserCreationRequest;
import develop.circlek.dto.UserUpdateRequest;
import develop.circlek.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
