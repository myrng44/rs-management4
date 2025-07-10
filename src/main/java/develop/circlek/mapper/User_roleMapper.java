package develop.circlek.mapper;

import develop.circlek.dto.user.User_roleCreationRequest;
import develop.circlek.dto.user.User_roleUpdateRequest;
import develop.circlek.entity.user.User_role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface User_roleMapper {
    User_role toUser_role(User_roleCreationRequest request);
    void updateUser_role(@MappingTarget User_role user_role, User_roleUpdateRequest request);
}
