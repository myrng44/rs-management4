package ck4.nvb.rsmanagement;

import ck4.nvb.rsmanagement.entity.Role;
import ck4.nvb.rsmanagement.entity.Users;
import ck4.nvb.rsmanagement.service.RoleService;
import ck4.nvb.rsmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitialize implements CommandLineRunner {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;


    @Override
    public void run(String... args) throws Exception {
        Role role1 = new Role("ADMIN");
        Role role2 = new Role("STAFF");
        Role role3 = new Role("MANAGEMENT");

        Users user1 = new Users("cong", "thanh");
        Users user2 = new Users("trong", "nghia");
        Users user3 = new Users("quang", "dat");
        Users users4 = new Users("Anh", "Thu");

        userService.saveUser(user1, role1);
        userService.saveUser(user1, role2);
        userService.saveUser(user2, role2);
        userService.saveUser(user3, role3);
        userService.saveUser(users4, role1);
    }

}
