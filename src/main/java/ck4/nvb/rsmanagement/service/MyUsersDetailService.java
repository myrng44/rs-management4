package ck4.nvb.rsmanagement.service;

import ck4.nvb.rsmanagement.entity.UserPrincipal;
import ck4.nvb.rsmanagement.entity.Users;
import ck4.nvb.rsmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUsersDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return new UserPrincipal(user);
    }
}
