package ck4.nvb.rsmanagement.core.auth.service;

import ck4.nvb.rsmanagement.core.auth.entity.Users;
import ck4.nvb.rsmanagement.core.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AppUsersDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users existingUser = userRepository.findByUsername(username) // đây là phương thức truy vấn custom từ UserRepository
                .orElseThrow(() -> new UsernameNotFoundException("Username not found" + username));
//        return new Users(existingUser.getUsername(), existingUser.getPassword(), new ArrayList<>()); // sai vì đây là của User của tôi
        // ở đây phải trả về 1 UserDetails cho nên sẽ phải trả về User của thưu viện org.springframework.security.core.userdetails.User,
        return new User(existingUser.getUsername(), existingUser.getPassword(), new ArrayList<>()); //new ArrayList ở đây là danh sách các quyền GranAuthority ở đây tạm thời để trống
    }
}
