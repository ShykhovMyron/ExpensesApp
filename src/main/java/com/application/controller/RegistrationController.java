package com.application.controller;

import com.application.entity.User;
import com.application.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    final static Logger logger = Logger.getLogger(RegistrationController.class);
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistrationPageInfo(User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@Valid User user,
                             BindingResult validResult,
                             Model model) {
        if (userService.createUser(user, validResult, model)) {
            return "redirect:/login";
        } else {
            return "registration";
        }
    }
}
