package org.sidorov.Spring_Boot_JPA_MVC_SECURITY.services.user;

import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.Role;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.User;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.repositories.RoleRepository;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private Role ROLE_ADMIN = new Role("ROLE_ADMIN");
    private Role ROLE_USER = new Role("ROLE_USER");

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        for (String role : user.getStringRoles()) {
            if (role != null && role.contains("ADMIN")) roles.add(ROLE_ADMIN);
            if (role != null && role.contains("USER")) roles.add(ROLE_USER);
        }
        user.setRoles(roles);
        userRepository.saveAndFlush(user);
    }

    @Override
    public User readUserByUsername(String username) {
        return userRepository.readUserByUsername(username);
    }

    @Override
    public User readUserById(long id) {
        return userRepository.readUserById(id);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(User user) {
        User oldUser = userRepository.readUserById(user.getId());
        Set<Role> roles = new HashSet<>();
        System.out.println(user);
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        for (String role : user.getStringRoles()) {
            if (role.contains("ADMIN")) roles.add(ROLE_ADMIN);
            if (role.contains("USER")) roles.add(ROLE_USER);
        }
        if (roles.isEmpty()) {
            user.setRoles(oldUser.getRoles());
        } else {
            user.setRoles(roles);
        }
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.readUserByUsername(username);
    }
}
