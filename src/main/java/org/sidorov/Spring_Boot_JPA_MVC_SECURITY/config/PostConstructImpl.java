package org.sidorov.Spring_Boot_JPA_MVC_SECURITY.config;

import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.Role;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.User;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.repositories.UserRepository;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class PostConstructImpl {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleService roleService;

    @Autowired
    public PostConstructImpl(PasswordEncoder passwordEncoder,
                             UserRepository userRepository,
                             RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @PostConstruct
    @Transactional
    public void addUsersAndRoles() {
        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleService.addRole(roleUser);
        roleService.addRole(roleAdmin);

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        User user = new User(
                "pavel@mail.ru",
                "Pavel",
                "Sorosov",
                (short) 25,
                passwordEncoder.encode("password"),
                roles);
        userRepository.saveAndFlush(user);
        roles.add(roleAdmin);
        User admin = new User(
                "alex@mail.ru",
                "Alex",
                "Crud",
                (short) 32,
                passwordEncoder.encode("password"),
                roles);
        userRepository.saveAndFlush(admin);
    }
}
