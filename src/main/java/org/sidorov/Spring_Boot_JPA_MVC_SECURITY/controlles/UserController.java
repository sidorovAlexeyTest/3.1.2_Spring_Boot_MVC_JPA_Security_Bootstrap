package org.sidorov.Spring_Boot_JPA_MVC_SECURITY.controlles;

import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.model.User;
import org.sidorov.Spring_Boot_JPA_MVC_SECURITY.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private UserService userServiceImpl;

    @Autowired
    public void setUserService(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PreAuthorize("#username == authentication.principal.username")
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ModelAndView getUserHome(@PathVariable("username") String username, ModelAndView modelAndView){
        User user = userServiceImpl.readUserByUsername(username);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user_page");
        return modelAndView;
    }
}
