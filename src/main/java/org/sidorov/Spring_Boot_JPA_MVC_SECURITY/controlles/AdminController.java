package org.sidorov.Spring_Boot_JPA_MVC_SECURITY.controlles;

import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.Role;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.User;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.services.role.RoleService;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    private UserService userServiceImpl;
    private static Role ROLE_ADMIN;
    private static Role ROLE_USER;

    @Autowired
    public AdminController(UserService userServiceImpl,
                           RoleService roleServiceImpl){
        this.userServiceImpl = userServiceImpl;
        ROLE_ADMIN = roleServiceImpl.readRoleByRole("ROLE_ADMIN");
        ROLE_USER = roleServiceImpl.readRoleByRole("ROLE_USER");
    }

    //Admin page users
    @RequestMapping()
    @Transactional
    public ModelAndView getAdminUsersPage(ModelAndView modelAndView, ModelMap modelMap) {
        List<User> userList = userServiceImpl.readAll();
        modelMap.addAttribute("userList", userList);
        modelAndView.addAllObjects(modelMap);
        modelAndView.setViewName("admin_page_users");
        return modelAndView;
    }

    //Admin page new user
    @RequestMapping(path = "/new_user")
    public ModelAndView getAdminNewUser(ModelAndView modelAndView) {
        modelAndView.setViewName("admin_page_new_user");
        return modelAndView;
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("newUser") User newUser) {
        newUser.setEnabled(true);
        userServiceImpl.addUser(newUser);
        return "redirect:/admin";
    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateUser(@ModelAttribute("user") User user,
                             @PathVariable("id") long id) {
        user.setEnabled(true);
        userServiceImpl.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }

    @ModelAttribute(name = "currentUser")
    public User getCurrentUser(Principal principal) {
        return userServiceImpl.readUserByUsername(principal.getName());
    }

    @ModelAttribute(name = "user_roles")
    public Set<Role> userName() {
        return Set.of(ROLE_ADMIN, ROLE_USER);
    }

    @ModelAttribute(name = "user")
    public User emptyUser(){
        return new User();
    }
}
