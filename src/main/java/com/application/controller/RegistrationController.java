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
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(User user) {
        logger.info("Come to registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@Valid User user,
                             BindingResult result,
                             Model model) {
        logger.info("Got request :" + model);

        if (result.hasErrors()) {
            model.addAttribute("errors",result.getAllErrors());
            return "registration";
        }
        if (userService.createUser(user)) {
            logger.info("User username:" + user.getUsername() + " password:" + user.getPassword() + " created");
            return "redirect:/login";
        } else {
            logger.info("User username:" + user.getUsername() + " password:" + user.getPassword() + " alreadydi  exist");
            model.addAttribute("message", "User exists!");
            return "registration";
        }
    }
}
