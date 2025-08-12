package ck4.nvb.rsmanagement.core.auth.service;

import ck4.nvb.rsmanagement.core.auth.entity.Users;
import ck4.nvb.rsmanagement.core.auth.io.ProfileRequest;
import ck4.nvb.rsmanagement.core.auth.io.ProfileResponse;
import ck4.nvb.rsmanagement.core.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
//@RequiredArgsConstructor
public class ProfileServiceIpm implements ProfileService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        Users newUser = convertToUserEntity(profileRequest);
        if (!userRepository.existsByUsername(profileRequest.getUsername())) {
            newUser = userRepository.save(newUser);
            return convertToProfileResponse(newUser);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
    }

    @Override
    public ProfileResponse getProfile(String username) {
        Users existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
        return convertToProfileResponse(existingUser);
    }

    private ProfileResponse convertToProfileResponse(Users newUser) {
        return ProfileResponse.builder()
                .username(newUser.getUsername())
                .fullName(newUser.getFullName())
                .isAccountVerified(newUser.isAccountVerified())
                .build();
    }

    private Users convertToUserEntity(ProfileRequest profileRequest) {
        return Users.builder()
                .username(profileRequest.getUsername())
                .fullName(profileRequest.getFullName())
                .password(passwordEncoder.encode(profileRequest.getPassword()))
                .isAccountVerified(false)
//                .id(snowFlakeIdGenerator.generateId())
                .build();
    }


}
