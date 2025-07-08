package ck4.nvb.rsmanagement.service;

import ck4.nvb.rsmanagement.dto.UserDTO;
import ck4.nvb.rsmanagement.dto.UserResponseDTO;
import ck4.nvb.rsmanagement.entity.Role;
import ck4.nvb.rsmanagement.entity.UserRole;
import ck4.nvb.rsmanagement.entity.Users;
import ck4.nvb.rsmanagement.repository.RoleRepository;
import ck4.nvb.rsmanagement.repository.UserRepository;
import ck4.nvb.rsmanagement.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtService jwtService;

    public void saveUser(Users user, Role role) {
        UserRole userRole = new UserRole();
        roleService.saveRole(role);
        userRole.setRole(role);
        user.addUserRole(userRole);
        userRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public String verify(Users user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToke(user.getUsername());
        } else {
            return "fail";
        }
    }

    private UserResponseDTO toUserResponseDTO(Users user) {
        List<String> roleNames = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());

        return new UserResponseDTO(user.getUsername(), roleNames);
    }

    public List<UserResponseDTO> getAllUserDTO() {
        List<Users> users = getAllUsers();
        List<UserResponseDTO> result = new ArrayList<>();
        for (Users user : users) {
            UserResponseDTO userResponseDTO = toUserResponseDTO(user);
            result.add(userResponseDTO);
        }
        return result;
    }

    public Users saveUserDTO(UserDTO dto) {
        var user = new Users();
        user.setUsername(dto.username());
        user.setPassword(dto.password());

        for (String roleName : dto.role()) {
            Role role = roleService.findByName(roleName);
            if (role == null) {
                throw new RuntimeException("Role not found: " + roleName);
            }
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUsers(user);

            user.addUserRole(userRole);
        }
        return userRepository.save(user);
    }
}
