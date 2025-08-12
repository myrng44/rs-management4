package ck4.nvb.rsmanagement.core.auth.io;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse extends Dto {
    private String username;
    private String fullName;
    private boolean isAccountVerified;
}
