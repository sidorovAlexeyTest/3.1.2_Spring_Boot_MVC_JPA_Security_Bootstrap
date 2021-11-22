package org.sidorov.Spring_Boot_JPA_MVC_SECURITY.config;

import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.Role;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.User;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.repositories.UserRepository;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.services.role.RoleService;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class PostConstructImpl {

    private UserService userServiceImpl;
    private RoleService roleServiceImpl;

    @Autowired
    public PostConstructImpl(UserService userServiceImpl,
                             RoleService roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @PostConstruct
    @Transactional
    public void addUsersAndRoles() {
        Set<Role> roles = new HashSet<>();
        User user = new User(
                "pavel@mail.ru",
                "Pavel",
                "Sorosov",
                (short) 25,
                "password",
                roles);
        user.getStringRoles()[0] = "USER";
        userServiceImpl.addUser(user);
        User admin = new User(
                "alex@mail.ru",
                "Alex",
                "Crud",
                (short) 32,
                "password",
                roles);
        admin.getStringRoles()[0] = "USER";
        admin.getStringRoles()[1] = "ADMIN";
        userServiceImpl.addUser(admin);
    }
}
