package ck4.nvb.rsmanagement.dto;

import java.util.List;

public record UserDTO(String username,
                      String password,
                      List<String> role) {
}
