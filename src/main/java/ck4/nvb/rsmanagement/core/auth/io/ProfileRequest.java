package ck4.nvb.rsmanagement.core.auth.io;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.core.auth.entity.Users;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest extends EntityDto<Long> implements CreateInput<Users> {
    @NotNull(message = "Username not should empty")
    private String username;

    private String fullName;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    //chuyển thằng profile request thành thằng UsersEntity
    @Override
    public Users mapToEntity() {
        return new ModelMapper().map(this, Users.class);
    }
}
