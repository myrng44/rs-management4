package ck4.nvb.rsmanagement.core.module.users.user.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.users.user.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserUpdateDto extends BaseUserDto implements UpdateInput<User> {

    private String fullName;
    private String email;
    private String phone;
    private Long storeId;
    private boolean enabled;

    @Override
    public boolean mapToEntity(User entity) {
        boolean isModified = false;
        if (!fullName.equals(entity.getName())) {
            entity.setName(fullName);
            isModified = true;
        }
        if (!email.equals(entity.getEmail())) {
            entity.setEmail(email);
            isModified = true;
        }
        if (!phone.equals(entity.getPhone())) {
            entity.setPhone(phone);
            isModified = true;
        }
        if ((long) storeId != entity.getStoreId()) {
            entity.setStoreId(storeId);
            isModified = true;
        }
        if (enabled != entity.getEnable()) {
            entity.setEnable(enabled);
            isModified = true;
        }
        return isModified;
    }
}
