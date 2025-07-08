package ck4.nvb.rsmanagement.controller;

import ck4.nvb.rsmanagement.dto.UserDTO;
import ck4.nvb.rsmanagement.dto.UserResponseDTO;
import ck4.nvb.rsmanagement.entity.Users;
import ck4.nvb.rsmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return userService.verify(user);
    }

    @PostMapping("/add")
    public Users post(@RequestBody UserDTO dto) {
        return userService.saveUserDTO(dto);
    }


    @GetMapping("/dto")
    public List<UserResponseDTO> getUsers() {
        return userService.getAllUserDTO();
    }
}
