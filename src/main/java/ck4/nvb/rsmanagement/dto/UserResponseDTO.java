package ck4.nvb.rsmanagement.dto;

import ck4.nvb.rsmanagement.entity.UserRole;

import java.util.List;

public record UserResponseDTO(String username,
                              List<String> role) {
}