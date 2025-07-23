package ck4.nvb.rsmanagement.auth.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
    @NotNull(message = "Username not should empty")
    private String username;

    private String fullName;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
